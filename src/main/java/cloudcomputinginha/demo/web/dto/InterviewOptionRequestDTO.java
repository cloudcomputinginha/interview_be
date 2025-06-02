package cloudcomputinginha.demo.web.dto;

import cloudcomputinginha.demo.domain.enums.VoiceType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class InterviewOptionRequestDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterviewOptionUpdateDTO {
        @NotNull
        private VoiceType voiceType;
        @NotNull
        private Integer questionNumber;
        @NotNull
        private Integer answerTime;
    }
}
