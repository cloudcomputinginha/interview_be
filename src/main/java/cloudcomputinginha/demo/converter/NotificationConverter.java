package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.Notification;
import cloudcomputinginha.demo.domain.embedded.Url;
import cloudcomputinginha.demo.domain.enums.NotificationType;
import cloudcomputinginha.demo.web.dto.NotificationResponseDTO;

public class NotificationConverter {
    public static Notification toNotification(Member receiver, NotificationType notificationType, String message, String url) {
        return Notification.builder()
                .receiver(receiver)
                .type(notificationType)
                .url(new Url(url))
                .message(message)
                .build();
    }

    public static NotificationResponseDTO.NotificationDTO toNotificationDTO(Notification notification) {
        return NotificationResponseDTO.NotificationDTO.builder()
                .type(notification.getType())
                .url(notification.getUrl().getUrl())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
