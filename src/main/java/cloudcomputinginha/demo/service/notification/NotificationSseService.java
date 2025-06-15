package cloudcomputinginha.demo.service.notification;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationSseService {
    public SseEmitter subscribe(Long memberId, String lastEventId);

    public void sendNotification(SseEmitter emitter, String emitterId, String eventId, Object data);

    public String createId(Long memberId);

    public void sendToMyAllEmitters(String memberId, String eventId, Object data);
}
