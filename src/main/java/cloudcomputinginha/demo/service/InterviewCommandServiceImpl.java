package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.apiPayload.code.handler.CoverletterHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.InterviewHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.MemberHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.ResumeHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.converter.InterviewConverter;
import cloudcomputinginha.demo.converter.MemberInterviewConverter;
import cloudcomputinginha.demo.domain.*;
import cloudcomputinginha.demo.repository.*;
import cloudcomputinginha.demo.web.dto.InterviewRequestDTO;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
