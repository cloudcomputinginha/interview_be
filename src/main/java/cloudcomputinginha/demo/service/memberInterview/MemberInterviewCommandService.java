package cloudcomputinginha.demo.service.memberInterview;

import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.domain.enums.InterviewStatus;
import cloudcomputinginha.demo.web.dto.MemberInterviewRequestDTO;


public interface MemberInterviewCommandService {
    MemberInterview changeMemberInterviewStatus(Long interviewId, Long memberId, InterviewStatus status);

    MemberInterview createMemberInterview(Long interviewId, MemberInterviewRequestDTO.createMemberInterviewDTO createMemberInterviewDTO);

    void finalizeStatuses(Long interviewId);
}
