package cloudcomputinginha.demo.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class QnaRequestDTO {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class createQnaDTO {
        @NotBlank
        private String question;
        @NotBlank
        private String answer;
    }
}
