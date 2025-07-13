package cloudcomputinginha.demo.service.interview;

import cloudcomputinginha.demo.apiPayload.code.handler.CoverletterHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.InterviewHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.MemberHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.ResumeHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.converter.InterviewConverter;
import cloudcomputinginha.demo.converter.MemberInterviewConverter;
import cloudcomputinginha.demo.domain.*;
import cloudcomputinginha.demo.domain.enums.InterviewFormat;
import cloudcomputinginha.demo.repository.*;
import cloudcomputinginha.demo.scheduler.InterviewScheduler;
import cloudcomputinginha.demo.service.memberInterview.MemberInterviewCommandService;
import cloudcomputinginha.demo.service.memberInterview.MemberInterviewSocketService;
import cloudcomputinginha.demo.web.dto.InterviewRequestDTO;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus.INTERVIEW_END_TIME_INVALID;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewCommandServiceImpl implements InterviewCommandService {

    private final InterviewRepository interviewRepository;
    private final MemberInterviewCommandService memberInterviewCommandService;
    private final InterviewOptionRepository interviewOptionRepository;
    private final MemberRepository memberRepository;
    private final ResumeRepository resumeRepository;
    private final CoverletterRepository coverletterRepository;
    private final MemberInterviewRepository memberInterviewRepository;
    private final QnaRepository qnaRepository;
    private final MemberInterviewSocketService memberInterviewSocketService;

    private final InterviewScheduler interviewScheduler;

    @Override
    @Transactional
    public Interview terminateInterview(Long memberId, Long interviewId, InterviewRequestDTO.endInterviewRequestDTO endInterviewRequestDTO) {
        Interview interview = interviewRepository.getReferenceWithInterviewOptionById(interviewId);

        if (interview.getEndedAt() != null) {
            throw new InterviewHandler(ErrorStatus.INTERVIEW_ALREADY_TERMINATED);
        }

        if (endInterviewRequestDTO.getEndedAt().isBefore(interview.getStartedAt())) {
            throw new InterviewHandler(INTERVIEW_END_TIME_INVALID);
        }

        // InterviewOption 종료 시간 갱신
        interview.updateEndedAt(endInterviewRequestDTO.getEndedAt());
        interviewRepository.save(interview);
        return interview;
    }

    @Override
    @Transactional
    public InterviewResponseDTO.InterviewCreateResultDTO createInterview(InterviewRequestDTO.InterviewCreateDTO request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Resume resume = resumeRepository.findById(request.getResumeId())
                .orElseThrow(() -> new ResumeHandler(ErrorStatus.RESUME_NOT_FOUND));

        Coverletter coverletter = coverletterRepository.findById(request.getCoverLetterId())
                .orElseThrow(() -> new CoverletterHandler(ErrorStatus.COVERLETTER_NOT_FOUND));

        memberInterviewCommandService.validateCoverletterOwnership(coverletter.getId(), memberId);
        memberInterviewCommandService.validateResumeOwnership(resume.getId(), memberId);

        InterviewOption interviewOption = InterviewConverter.toInterviewOption(request);
        interviewOptionRepository.save(interviewOption);

        Interview interview = InterviewConverter.toInterview(request, interviewOption, member);
        // TODO: 초대 메일 검증 코드 + 초대 알림 전송
        interviewRepository.save(interview);

        MemberInterview memberInterview = MemberInterviewConverter.toMemberInterview(member, interview, resume, coverletter);
        memberInterviewRepository.save(memberInterview);

        // 인터뷰 스케줄링
        interviewScheduler.scheduleInterviewStart(
                interview.getId(),
                interview.getStartedAt()
        );

        // 인터뷰 리마인드 스케줄링
        interviewScheduler.scheduleInterviewReminderIfNotExists(
                interview.getId(),
                interview.getStartedAt(),
                Duration.ofDays(1),
                "D1"
        );

        interviewScheduler.scheduleInterviewReminderIfNotExists(
                interview.getId(),
                interview.getStartedAt(),
                Duration.ofDays(30),
                "M30"
        );

        return InterviewConverter.createInterview(interview);
    }

    @Override
    public InterviewResponseDTO.InterviewStartResponseDTO startInterview(Long memberId, Long interviewId, Boolean isAutoMaticStart) {
        Interview interviewWithOption = interviewRepository.getReferenceWithInterviewOptionById(interviewId);

        boolean isGroupInterview = interviewWithOption.getInterviewOption().getInterviewFormat().equals(InterviewFormat.GROUP);

        List<MemberInterview> memberInterviews = isGroupInterview ?
            memberInterviewRepository.findInprogressByInterviewId(interviewId) :
            memberInterviewRepository.findByInterviewId(interviewId);

        List<Long> coverletterIds = memberInterviews.stream()
                .map(mi -> mi.getCoverletter().getId())
                .toList();
        List<Qna> allQnas = qnaRepository.findAllByCoverletterIds(coverletterIds);

        List<Long> memberIds = memberInterviews.stream()
                .map(mi -> mi.getMember().getId())
                .toList();

        if(isGroupInterview) {

            // 참가자들에게 참가알림 발송
            memberInterviewSocketService.enterInterview(interviewId);
        }

        if (!isAutoMaticStart) {
            interviewScheduler.cancelScheduledInterview(interviewId);
            interviewWithOption.updateStartedAt(LocalDateTime.now());
        }

        return InterviewConverter.toInterviewStartResponseDTO(interviewWithOption, memberInterviews, allQnas);
    }

    @Override
    @Transactional
    public InterviewResponseDTO.InterviewUpdateResponseDTO updateInterview(Long memberId, Long interviewId, InterviewRequestDTO.InterviewUpdateDTO request) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewHandler(ErrorStatus.INTERVIEW_NOT_FOUND));

        if (!interview.getHostId().equals(memberId)) {
            throw new InterviewHandler(ErrorStatus.INTERVIEW_NO_PERMISSION);
        }

        if (interview.getStartedAt().isBefore(LocalDateTime.now())) {
            throw new InterviewHandler(ErrorStatus.INTERVIEW_ALREADY_TERMINATED);
        }

        InterviewFormat interviewFormat = interview.getInterviewOption().getInterviewFormat();

        // 일대일, 일대다 공통 수정
        interview.updateName(request.getName());
        interview.updateDescription(request.getDescription());

        if (interviewFormat == InterviewFormat.GROUP) {
            interview.updateMaxParticipants(request.getMaxParticipants());
            interview.updateIsOpen(request.getIsOpen());
        } else {
            interview.updateMaxParticipants(1);
            interview.updateIsOpen(false);
        }
        return InterviewConverter.toInterviewUpdateResponseDTO(interview);
    }
}
