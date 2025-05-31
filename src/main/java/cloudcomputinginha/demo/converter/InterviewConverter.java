package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.Interview;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;

public class InterviewConverter {
    public static InterviewResponseDTO.InterviewEndResultDTO toInterviewEndResultDTO(Interview interview) {
        return InterviewResponseDTO.InterviewEndResultDTO.builder()
                .interviewId(interview.getId())
                .endedAt(interview.getInterviewOption().getEndedAt())
                .build();
    }
}
