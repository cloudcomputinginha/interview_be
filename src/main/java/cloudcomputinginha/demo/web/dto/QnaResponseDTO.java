package cloudcomputinginha.demo.web.dto;

import lombok.*;

public class QnaResponseDTO {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class QnaDTO {
        private String question;
        private String answer;
    }
}
