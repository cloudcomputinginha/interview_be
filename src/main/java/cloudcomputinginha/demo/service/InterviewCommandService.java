package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.converter.InterviewConverter;
import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.web.dto.InterviewRequestDTO;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;

public interface InterviewCommandService {
    Interview terminateInterview(Long interviewId, InterviewRequestDTO.endInterviewRequestDTO endInterviewRequestDTO);
    InterviewResponseDTO.InterviewCreateResultDTO createInterview(InterviewRequestDTO.InterviewCreateDTO request, Long memberId);
}
