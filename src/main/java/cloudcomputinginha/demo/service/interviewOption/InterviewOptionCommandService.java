package cloudcomputinginha.demo.service.interviewOption;

import cloudcomputinginha.demo.web.dto.request.InterviewOptionRequestDTO;
import cloudcomputinginha.demo.web.dto.response.InterviewOptionResponseDTO;

public interface InterviewOptionCommandService {
    InterviewOptionResponseDTO.InterviewOptionUpdateResponseDTO updateInterviewOption(Long memberId, Long interviewId, InterviewOptionRequestDTO.InterviewOptionUpdateDTO request);
}
