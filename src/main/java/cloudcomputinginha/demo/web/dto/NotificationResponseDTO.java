package cloudcomputinginha.demo.web.dto;

import cloudcomputinginha.demo.domain.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class NotificationListDTO {
        private List<NotificationDTO> notifications;
        private Integer size;
    }
}
