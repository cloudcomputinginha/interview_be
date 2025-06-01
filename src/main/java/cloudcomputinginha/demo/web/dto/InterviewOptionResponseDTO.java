package cloudcomputinginha.demo.web.dto;

import cloudcomputinginha.demo.domain.enums.InterviewFormat;
import cloudcomputinginha.demo.domain.enums.InterviewType;
import cloudcomputinginha.demo.domain.enums.VoiceType;
import lombok.*;

public class InterviewOptionResponseDTO {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InterviewOptionDTO {
        private InterviewFormat interviewFormat;
        private InterviewType interviewType;
        private VoiceType voiceType;
        private int questionNumber;
        private int answerTime;
    }
}
