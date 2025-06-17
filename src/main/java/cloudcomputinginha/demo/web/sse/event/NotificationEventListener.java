package cloudcomputinginha.demo.web.sse.event;

import cloudcomputinginha.demo.web.sse.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final SseService sseService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleCreateNotification(NotificationEvent event) {

        sseService.sendToMyAllEmitters(event.getReceiverId(), event.getEventId(), event.getNotificationDTO());
    }
}
