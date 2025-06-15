package cloudcomputinginha.demo.service.interview;

import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;

import java.util.List;

public interface InterviewQueryService {
    List<InterviewResponseDTO.InterviewGroupCardDTO> getGroupInterviewCards(Long memberId);

    InterviewResponseDTO.GroupInterviewDetailDTO getGroupInterviewDetail(Long memberId, Long interviewId);
}
