package cloudcomputinginha.demo.service.notification;

import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.Notification;
import cloudcomputinginha.demo.domain.enums.NotificationType;

public interface NotificationCommandService {
    public void createNotificationAndSend(Member receiver, NotificationType notificationType, String content, String url);

    public void send(Member receiver, Notification notification);
}
