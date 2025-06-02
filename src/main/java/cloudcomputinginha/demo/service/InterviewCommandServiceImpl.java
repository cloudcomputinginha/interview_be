package cloudcomputinginha.demo.service;

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
import cloudcomputinginha.demo.web.dto.InterviewRequestDTO;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public Interview terminateInterview(Long interviewId, InterviewRequestDTO.endInterviewRequestDTO endInterviewRequestDTO) {
        Interview interview = interviewRepository.getReferenceWithInterviewOptionById(interviewId);

        if (interview.getEndedAt() != null) {
            throw new InterviewHandler(ErrorStatus.INTERVIEW_ALREADY_TERMINATED);
        }


        if (endInterviewRequestDTO.getEndedAt().isBefore(interview.getStartedAt())) {
            throw new InterviewHandler(INTERVIEW_END_TIME_INVALID);
        }

        // 사용자 상태 업데이트는 MemberInterviewService에 위임
        memberInterviewCommandService.finalizeStatuses(interviewId);

        // InterviewOption 종료 시간 갱신
        interview.changeEndedAt(endInterviewRequestDTO.getEndedAt());
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

        InterviewOption interviewOption = InterviewConverter.toInterviewOption(request);
        interviewOptionRepository.save(interviewOption);

        Interview interview = InterviewConverter.toInterview(request, interviewOption, member);
        interviewRepository.save(interview);

        MemberInterview memberInterview = MemberInterviewConverter.toMemberInterview(member, interview, resume, coverletter);
        memberInterviewRepository.save(memberInterview);

        return InterviewConverter.createInterview(interview);
    }

    @Override
    public InterviewResponseDTO.InterviewStartResponseDTO startInterview(Long interviewId) {
        Interview interviewWithOption = interviewRepository.getReferenceWithInterviewOptionById(interviewId);

        List<MemberInterview> memberInterviews = memberInterviewRepository.findInprogressByInterviewId(interviewId);

        List<Long> coverletterIds = memberInterviews.stream()
                .map(mi -> mi.getCoverletter().getId())
                .toList();
        List<Qna> allQnas = qnaRepository.findAllByCoverletterIds(coverletterIds);

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
