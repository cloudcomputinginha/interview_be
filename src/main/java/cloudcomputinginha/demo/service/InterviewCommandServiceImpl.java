package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.apiPayload.code.handler.InterviewHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.repository.InterviewOptionRepository;
import cloudcomputinginha.demo.repository.InterviewRepository;
import cloudcomputinginha.demo.repository.MemberRepository;
import cloudcomputinginha.demo.web.dto.InterviewRequestDTO;
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

    @Override
    @Transactional
    public Interview terminateInterview(Long interviewId, InterviewRequestDTO.endInterviewRequestDTO endInterviewRequestDTO) {
        Interview interview = interviewRepository.getReferenceWithInterviewOptionById(interviewId);

        if (interview.getInterviewOption().getEndedAt() != null) {
            throw new InterviewHandler(ErrorStatus.INTERVIEW_ALREADY_TERMINATED);
        }


        if (endInterviewRequestDTO.getEndedAt().isBefore(interview.getInterviewOption().getScheduledAt())) {
            throw new InterviewHandler(INTERVIEW_END_TIME_INVALID);
        }

        // 사용자 상태 업데이트는 MemberInterviewService에 위임
        memberInterviewCommandService.finalizeStatuses(interviewId);

        // InterviewOption 종료 시간 갱신
        interview.getInterviewOption().changeEndedAt(endInterviewRequestDTO.getEndedAt());
        interviewRepository.save(interview);
        return interview;
    }

    @Override
    @Transactional
    public InterviewResponseDTO.InterviewCreateResultDTO createInterview(InterviewRequestDTO.InterviewCreateDTO request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
    }
}
