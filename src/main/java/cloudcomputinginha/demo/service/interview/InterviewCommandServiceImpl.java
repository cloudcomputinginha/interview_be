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
import cloudcomputinginha.demo.domain.enums.InterviewStatus;
import cloudcomputinginha.demo.domain.enums.StartType;
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
    private final MemberInterviewSocketService memberInterviewSocketService;

    private final InterviewScheduler interviewScheduler;

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
        Interview interview = InterviewConverter.toInterview(request, member);
        interview.setInterviewOption(interviewOption);
        // TODO: 초대 메일 검증 코드 + 초대 알림 전송
        interviewRepository.save(interview);

        MemberInterview memberInterview = MemberInterviewConverter.toMemberInterview(member, interview, resume, coverletter);
        interview.addMemberInterview(memberInterview);
        member.addMemberInterview(memberInterview);

        // 면접 시작 종류가 SCHEDULED인 경우, 시작과 리마인더를 스케줄링
        if (interview.getStartType().equals(StartType.SCHEDULED)) {
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
        }
        return InterviewConverter.createInterview(interview);
    }

    @Override
    public InterviewResponseDTO.InterviewStartResponseDTO startInterview(Long memberId, Long interviewId, Boolean isAutomaticStart) {
        // 1. 면접과 면접 옵션 조회
        Interview interviewWithOption = interviewRepository.findWithInterviewOptionById(interviewId);

        // 2. 면접 포맷(그룹/개인)에 따라 내부 로직을 진행 후, 면접 참가자 리턴 받기
        List<MemberInterview> inProgressMemberInterviews;
        if (interviewWithOption.getInterviewOption().getInterviewFormat().equals(InterviewFormat.GROUP)) {
            inProgressMemberInterviews = startGroupInterview(memberId, interviewWithOption);
        } else {
            inProgressMemberInterviews = startPersonalInterview(memberId, interviewWithOption);
        }

        // 3. 만약 면접을 사용자가 수동 시작하는 경우라면 모든 스케쥴을 취소한다.
        if (!isAutomaticStart) {
            interviewScheduler.cancelScheduledInterview(interviewId);
        }

        // 4. 면접 시작 시간을 정확히 업데이트
        interviewWithOption.updateStartedAt(LocalDateTime.now());
        return InterviewConverter.toInterviewStartResponseDTO(interviewWithOption, inProgressMemberInterviews);
    }

    private List<MemberInterview> startPersonalInterview(Long memberId, Interview interview) {
        // 2-1. 면접 참가자 조회
        MemberInterview memberInterview = memberInterviewRepository.findByMemberIdAndInterviewId(memberId, interview.getId())
                .orElseThrow(() -> new InterviewHandler(ErrorStatus.MEMBER_INTERVIEW_NOT_FOUND));

        // 2-2. 참가자가 자소서, 이력서가 모두 존재하는지 확인(둘 다 필수여야 함)
        if (memberInterview.getResume() == null || memberInterview.getCoverletter() == null) {
            throw new InterviewHandler(ErrorStatus.INTERVIEW_DOCUMENTS_NOT_FOUND);
        }

        // 2-3. 참가자의 memberInterview 상태 변경
        memberInterview.updateStatus(InterviewStatus.DONE);

        return List.of(memberInterview);
    }

    private List<MemberInterview> startGroupInterview(Long memberId, Interview interview) {
        // 2-1. 현재 면접 대기실에 입장한(상태가 IN_PROGRESS인) 참가자들 조회
        Long interviewId = interview.getId();
        List<MemberInterview> memberInterviews = memberInterviewRepository.findByInterviewId(interviewId);
        List<MemberInterview> inProgressMemberInterviews = memberInterviews.stream()
                .filter(mi -> mi.getStatus() == InterviewStatus.IN_PROGRESS)
                .toList();

        // 2-2. 현재 대기실에 입장한 참가자들 중 해당 메서드를 호출한 사용자가 없으면 예외 발생
        if (inProgressMemberInterviews.stream()
                .noneMatch(mi -> mi.getMember().getId().equals(memberId))) {
            throw new InterviewHandler(ErrorStatus.INTERVIEW_NO_PERMISSION);
        }

        // 2-3. 참가자들의 자소서, 이력서가 모두 존재하는지 확인(둘 다 필수여야 함)
        if (inProgressMemberInterviews.stream()
                .anyMatch(mi -> mi.getResume() == null || mi.getCoverletter() == null)) {
            throw new InterviewHandler(ErrorStatus.INTERVIEW_DOCUMENTS_NOT_FOUND);
        }

        // 2-4. 면접의 모든 memberInterview의 상태 변경 & 참가알림 발송
        memberInterviewSocketService.enterInterview(interviewId, memberInterviews);

        return inProgressMemberInterviews;
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

    @Override
    @Transactional
    public Interview terminateInterview(Long memberId, Long interviewId, InterviewRequestDTO.endInterviewRequestDTO endInterviewRequestDTO) {
        Interview interview = interviewRepository.findWithInterviewOptionById(interviewId);

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
}
