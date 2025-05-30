package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.apiPayload.code.handler.MemberInterviewHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.repository.MemberInterviewRepository;
import cloudcomputinginha.demo.web.dto.MemberInterviewRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberInterviewCommandServiceImpl implements MemberInterviewCommandService {
    private final MemberInterviewRepository memberInterviewRepository;

    @Override
    public MemberInterview changeMemberInterviewStatus(Long interviewId, MemberInterviewRequestDTO.changeMemberStatusDTO memberInterviewRequestDTO) {
        MemberInterview memberInterview = memberInterviewRepository.findByMemberIdAndInterviewId(memberInterviewRequestDTO.getMemberId(), interviewId)
                .orElseThrow(() -> new MemberInterviewHandler(ErrorStatus.MEMBER_INTERVIEW_NOT_FOUND));

        memberInterview.changeStatus(memberInterviewRequestDTO.getStatus());
        memberInterviewRepository.save(memberInterview);
        return memberInterview;
    }
}
