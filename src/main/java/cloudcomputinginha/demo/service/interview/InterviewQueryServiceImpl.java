package cloudcomputinginha.demo.service.interview;

import cloudcomputinginha.demo.apiPayload.code.handler.InterviewHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.MemberHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.MemberInterviewHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.converter.InterviewConverter;
import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.repository.InterviewQueryRepository;
import cloudcomputinginha.demo.repository.InterviewRepository;
import cloudcomputinginha.demo.repository.MemberInterviewRepository;
import cloudcomputinginha.demo.repository.MemberRepository;
import cloudcomputinginha.demo.web.dto.response.InterviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterviewQueryServiceImpl implements InterviewQueryService {
    private final InterviewQueryRepository interviewQueryRepository;
    private final InterviewRepository interviewRepository;
    private final MemberInterviewRepository memberInterviewRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public List<InterviewResponseDTO.InterviewGroupCardDTO> getGroupInterviewCards(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return interviewQueryRepository.findGroupInterviewCards();
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewResponseDTO.GroupInterviewDetailDTO getGroupInterviewDetail(Long memberId, Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewHandler(ErrorStatus.INTERVIEW_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<MemberInterview> memberInterviewList = memberInterviewRepository.findAllByInterviewId(interviewId);
        MemberInterview myMemberInterview = memberInterviewList.stream()
                .filter(memberInterview -> memberInterview.getMember().getId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new MemberInterviewHandler(ErrorStatus.MEMBER_INTERVIEW_NOT_FOUND)); //데이터 구조상 하나만 존재할 수 있다.

        return InterviewConverter.toInterviewGroupDetailDTO(interview, myMemberInterview, memberInterviewList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Interview> getUpcomingInterviews(LocalDateTime currentTime) {
        return interviewRepository.findAllByStartedAtAfterAndEndedAtIsNull(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Interview> getInterview(Long interviewId) {
        return interviewRepository.findById(interviewId);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewResponseDTO.InterviewStartResponseDTO getStartInterviewInfo(Long memberId, Long interviewId) {
        // 1. 면접과 면접 옵션 조회
        Interview interviewWithOption = interviewRepository.findWithInterviewOptionById(interviewId)
                .orElseThrow(() -> new InterviewHandler(ErrorStatus.INTERVIEW_NOT_FOUND));

        // 2. 면접 참여자 조회
        List<MemberInterview> memberInterviews = memberInterviewRepository.findAllByInterviewId(interviewId);

        // 3. 현재 사용자가 면접 참여자인지 확인
        if (memberInterviews.stream()
                .noneMatch(mi -> mi.getMember().getId().equals(memberId))) {
            throw new InterviewHandler(ErrorStatus.INTERVIEW_NO_PERMISSION);
        }

        // 4. 참가자들의 자소서, 이력서 존재하는지 확인
        if (memberInterviews.stream()
                .anyMatch(mi -> !mi.hasResumeAndCoverLetter())) {
            throw new InterviewHandler(ErrorStatus.INTERVIEW_DOCUMENTS_NOT_FOUND);
        }
        return InterviewConverter.toInterviewStartResponseDTO(interviewWithOption, memberInterviews);
    }
}
