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
    public static class InterviewOptionPreviewDTO {
        private InterviewFormat interviewFormat;
        private InterviewType interviewType;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InterviewOptionDetailDTO {
        private InterviewFormat interviewFormat;
        private InterviewType interviewType;
        private VoiceType voiceType;
        private int questionNumber;
        private int answerTime;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InterviewOptionUpdateResponseDTO {
        private Long interviewId;
        private Long interviewOptionId;
        private VoiceType voiceType;
        private Integer questionNumber;
        private Integer answerTime;
    }
}
