package cloudcomputinginha.demo.service.interview;

import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;

import java.util.List;

public interface InterviewQueryService {
    List<InterviewResponseDTO.InterviewGroupCardDTO> getGroupInterviewCards();

    InterviewResponseDTO.GroupInterviewDetailDTO getGroupInterviewDetail(Long interviewId);
}
