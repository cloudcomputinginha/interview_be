package cloudcomputinginha.demo.repository;

import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;
import java.util.List;

public interface InterviewQueryRepository {
    List<InterviewResponseDTO.InterviewGroupCardDTO> findGroupInterviewCards();
}
