package cloudcomputinginha.demo.service.memberInterview;

import cloudcomputinginha.demo.apiPayload.code.handler.MemberHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.repository.MemberInterviewRepository;
import cloudcomputinginha.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberInterviewQueryServiceImpl implements MemberInterviewQueryService {
    private final MemberInterviewRepository memberInterviewRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<MemberInterview> getMyInterviews(Long memberId) {
        return memberInterviewRepository.findAllForMyPage(memberId);
    }

    @Override
    public MemberInterview getMyInterviewDocuments(Long memberId, Long interviewId) {
        return memberInterviewRepository.findWithDocumentsByMemberIdAndInterviewId(memberId, interviewId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_INTERVIEW_NOT_FOUND));
    }
}
