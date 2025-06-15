package cloudcomputinginha.demo.service.interview;

import cloudcomputinginha.demo.apiPayload.code.handler.InterviewHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.MemberHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.converter.InterviewConverter;
import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.repository.InterviewQueryRepository;
import cloudcomputinginha.demo.repository.InterviewRepository;
import cloudcomputinginha.demo.repository.MemberInterviewRepository;
import cloudcomputinginha.demo.repository.MemberRepository;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewQueryServiceImpl implements InterviewQueryService {
    private final InterviewQueryRepository interviewQueryRepository;
    private final InterviewRepository interviewRepository;
    private final MemberInterviewRepository memberInterviewRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public List<InterviewResponseDTO.InterviewGroupCardDTO> getGroupInterviewCards(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return interviewQueryRepository.findGroupInterviewCards();
    }

    @Override
    @Transactional
    public InterviewResponseDTO.GroupInterviewDetailDTO getGroupInterviewDetail(Long memberId, Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewHandler(ErrorStatus.INTERVIEW_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<MemberInterview> memberInterviewList = memberInterviewRepository.findByInterviewId(interviewId);
        return InterviewConverter.toInterviewGroupDetailDTO(interview, memberInterviewList);
    }
}
