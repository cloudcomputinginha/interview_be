package cloudcomputinginha.demo.web.dto;

import lombok.*;

import java.time.LocalDateTime;

public class CoverletterResponseDTO {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class createCoverletterDTO {
        private Long coverletterId;
        private LocalDateTime createdAt;
    }
}
