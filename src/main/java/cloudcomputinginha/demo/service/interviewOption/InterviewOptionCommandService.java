package cloudcomputinginha.demo.service.interviewOption;

import cloudcomputinginha.demo.web.dto.InterviewOptionRequestDTO;
import cloudcomputinginha.demo.web.dto.InterviewOptionResponseDTO;

public interface InterviewOptionCommandService {
    InterviewOptionResponseDTO.InterviewOptionUpdateResponseDTO updateInterviewOption(Long memberId, Long interviewId, InterviewOptionRequestDTO.InterviewOptionUpdateDTO request);
}
