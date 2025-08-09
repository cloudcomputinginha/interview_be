package cloudcomputinginha.demo.converter;

import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.Notification;
import cloudcomputinginha.demo.domain.embedded.DomainUrl;
import cloudcomputinginha.demo.domain.enums.NotificationType;
import cloudcomputinginha.demo.web.dto.NotificationResponseDTO;

import java.util.List;

public class NotificationConverter {
    public static Notification toNotification(Member receiver, NotificationType notificationType, String message, String url) {
        return Notification.builder()
                .receiver(receiver)
                .type(notificationType)
                .url(new DomainUrl(url))
                .message(message)
                .build();
    }

    public static NotificationResponseDTO.NotificationDTO toNotificationDTO(Notification notification) {
        return NotificationResponseDTO.NotificationDTO.builder()
                .type(notification.getType())
                .url(notification.getUrl().getDomainUrl())
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public static NotificationResponseDTO.NotificationListDTO toNotificationListDTO(List<Notification> notifications) {
        List<NotificationResponseDTO.NotificationDTO> notificationList = notifications.stream()
                .map(NotificationConverter::toNotificationDTO)
                .toList();
        return NotificationResponseDTO.NotificationListDTO.builder()
                .notifications(notificationList)
                .size(notificationList.size())
                .build();
    }

    public static NotificationResponseDTO.NotificationSSEDTO toNotificationSSEDTO(NotificationResponseDTO.NotificationDTO notificationDTO, Long unreadCount) {
        return NotificationResponseDTO.NotificationSSEDTO.builder()
                .notification(notificationDTO)
                .unreadCount(unreadCount)
                .build();
    }
}
