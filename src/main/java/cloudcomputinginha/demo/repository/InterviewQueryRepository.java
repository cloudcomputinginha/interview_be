package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.web.dto.response.InterviewResponseDTO;

import java.util.List;

public interface InterviewQueryRepository {
    List<InterviewResponseDTO.InterviewGroupCardDTO> findGroupInterviewCards();
}
