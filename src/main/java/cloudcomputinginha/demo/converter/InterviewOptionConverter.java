package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.InterviewOption;
import cloudcomputinginha.demo.web.dto.InterviewOptionResponseDTO;
import cloudcomputinginha.demo.web.dto.InterviewResponseDTO;

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

    public static InterviewOptionResponseDTO.InterviewOptionUpdateResponseDTO toInterviewOptionUpdateResponseDTO(InterviewOption interviewOption) {
        return InterviewOptionResponseDTO.InterviewOptionUpdateResponseDTO.builder()
                .interviewId(interviewOption.getInterview().getId())
                .interviewOptionId(interviewOption.getId())
                .voiceType(interviewOption.getVoiceType())
                .questionNumber(interviewOption.getQuestionNumber())
                .answerTime(interviewOption.getAnswerTime())
                .build();
    }
}
