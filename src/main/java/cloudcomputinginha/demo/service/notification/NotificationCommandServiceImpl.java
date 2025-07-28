package cloudcomputinginha.demo.service.notification;

import cloudcomputinginha.demo.apiPayload.code.handler.NotificationHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.converter.NotificationConverter;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.domain.Notification;
import cloudcomputinginha.demo.domain.enums.NotificationType;
import cloudcomputinginha.demo.repository.NotificationRepository;
import cloudcomputinginha.demo.web.sse.SseService;
import cloudcomputinginha.demo.web.sse.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationCommandServiceImpl implements NotificationCommandService {
    private final NotificationRepository notificationRepository;
    private final SseService sseService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 1. Notification을 생성 및 DB 저장
     * 2. NotificationEvent 발행
     * 3. @TransactionalEventListener가 커밋 이후 이벤트 감지
     * 4. 비동기로 sendToMyAllEmitters 실행
     * 5. SSE 알림 전송 완료
     *
     * @param receiver
     * @param notificationType
     * @param message
     * @param url
     */
    @Transactional
    @Override
    public void createNotificationAndSend(Member receiver, NotificationType notificationType, String message, String url) {
        Notification notification = NotificationConverter.toNotification(receiver, notificationType, message, url);
        receiver.addNotification(notification);
        notificationRepository.save(notification);

        String eventId = sseService.createId(receiver.getId());

        // 이벤트 전송은 commit 이후로 비동기 작업(위임)
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .receiverId(receiver.getId())
                .eventId(eventId)
                .notificationDTO(NotificationConverter.toNotificationDTO(notification))
                .build();
        eventPublisher.publishEvent(notificationEvent);
    }

    @Override
    @Transactional
    public void markAsRead(Long memberId, Long notificationId) {
        Notification notification = notificationRepository.findByIdAndReceiverId(notificationId, memberId)
                .orElseThrow(() -> new NotificationHandler(ErrorStatus.NOTIFICATION_NOT_OWNED));

        notification.markAsRead();
    }
}
