package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.repository.InterviewQueryRepository;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewQueryServiceImpl implements InterviewQueryService {
    private final InterviewQueryRepository interviewQueryRepository;

    @Override
    @Transactional
    public List<InterviewResponseDTO.InterviewGroupCardDTO> getGroupInterviewCards() {
        return interviewQueryRepository.findGroupInterviewCards();
    }
}
