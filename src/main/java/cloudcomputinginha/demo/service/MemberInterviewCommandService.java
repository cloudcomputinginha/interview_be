package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.domain.MemberInterview;
import cloudcomputinginha.demo.web.dto.MemberInterviewRequestDTO;

public interface MemberInterviewCommandService {
    public MemberInterview changeMemberInterviewStatus(Long interviewId, MemberInterviewRequestDTO.changeMemberStatusDTO memberInterviewRequestDTO);
}
