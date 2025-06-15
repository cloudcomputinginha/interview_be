package cloudcomputinginha.demo.service.notification;

import cloudcomputinginha.demo.converter.NotificationConverter;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.Notification;
import cloudcomputinginha.demo.domain.enums.NotificationType;
import cloudcomputinginha.demo.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationCommandServiceImpl implements NotificationCommandService {
    private final NotificationRepository notificationRepository;
    private final NotificationSseService notificationSseService;

    /**
     * Notification을 지금 생성하고 바로 알림을 주고 싶을 때 사용
     *
     * @param receiver
     * @param notificationType
     * @param content
     * @param url
     */
    @Transactional
    @Override
    public void createNotificationAndSend(Member receiver, NotificationType notificationType, String content, String url) {
        Notification notification = NotificationConverter.toNotification(receiver, notificationType, content, url);
        notificationRepository.save(notification);

        String receiverId = String.valueOf(receiver.getId());
        String eventId = notificationSseService.createId(receiver.getId());
        notificationSseService.sendToMyAllEmitters(receiverId, eventId, NotificationConverter.toNotificationDTO(notification));
    }

    /**
     * Notification을 미리 생성해두고 나중에 또는 정해진 시간에 알림을 주고 싶을 때 사용
     *
     * @param receiver
     * @param notification
     */
    @Override
    public void send(Member receiver, Notification notification) {
        String receiverId = String.valueOf(receiver.getId());
        String eventId = notificationSseService.createId(receiver.getId());
        notificationSseService.sendToMyAllEmitters(receiverId, eventId, NotificationConverter.toNotificationDTO(notification));
    }
}
