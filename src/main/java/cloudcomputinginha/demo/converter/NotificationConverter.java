package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.Notification;
import cloudcomputinginha.demo.domain.embedded.RelatedUrl;
import cloudcomputinginha.demo.domain.enums.NotificationType;
import cloudcomputinginha.demo.web.dto.NotificationResponseDTO;

public class NotificationConverter {
    public static Notification toNotification(Member receiver, NotificationType notificationType, String message, String url) {
        return Notification.builder()
                .receiver(receiver)
                .type(notificationType)
                .url(new RelatedUrl(url))
                .message(message)
                .build();
    }

    public static NotificationResponseDTO.NotificationeDTO toNotificationDTO(Notification notification) {
        return NotificationResponseDTO.NotificationeDTO.builder()
                .type(notification.getType())
                .url(notification.getUrl().getUrl())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
