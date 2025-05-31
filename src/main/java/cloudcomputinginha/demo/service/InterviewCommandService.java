package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.web.dto.InterviewRequestDTO;

public interface InterviewCommandService {
    Interview terminateInterview(Long interviewId, InterviewRequestDTO.endInterviewRequestDTO endInterviewRequestDTO);
}
