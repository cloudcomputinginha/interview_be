package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;
import java.util.List;

public interface InterviewQueryService {
    List<InterviewResponseDTO.InterviewGroupCardDTO> getGroupInterviewCards();
}
