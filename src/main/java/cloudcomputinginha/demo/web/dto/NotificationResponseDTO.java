package cloudcomputinginha.demo.web.dto;

import cloudcomputinginha.demo.domain.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

public class NotificationResponseDTO {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class NotificationDTO {
        private NotificationType type;
        private String url;
        private String message;
        private LocalDateTime createdAt;
    }
}
