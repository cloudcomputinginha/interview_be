package cloudcomputinginha.demo.service.notification;

import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.enums.NotificationType;

public interface NotificationCommandService {
    public void createNotificationAndSend(Member receiver, NotificationType notificationType, String content, String url);

    public void markAsRead(Long memberId, Long notificationId);
}
