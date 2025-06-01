package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.InterviewOption;
import cloudcomputinginha.demo.web.dto.InterviewOptionResponseDTO;

public class InterviewOptionConverter {
    public static InterviewOptionResponseDTO.InterviewOptionDTO toInterviewOptionDTO(InterviewOption interviewOption) {
        return InterviewOptionResponseDTO.InterviewOptionDTO.builder()
                .interviewFormat(interviewOption.getInterviewFormat())
                .interviewType(interviewOption.getInterviewType())
                .voiceType(interviewOption.getVoiceType())
                .questionNumber(interviewOption.getQuestionNumber())
                .answerTime(interviewOption.getAnswerTime())
                .build();
    }
}
