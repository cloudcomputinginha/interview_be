package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.web.dto.MemberInterviewRequestDTO;

public interface MemberInterviewCommandService {
    MemberInterview changeMemberInterviewStatus(Long interviewId, MemberInterviewRequestDTO.changeMemberStatusDTO memberInterviewRequestDTO);

    MemberInterview createMemberInterview(Long interviewId, MemberInterviewRequestDTO.createMemberInterviewDTO createMemberInterviewDTO);

    void finalizeStatuses(Long interviewId);
}
