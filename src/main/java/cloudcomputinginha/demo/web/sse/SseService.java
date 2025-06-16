package cloudcomputinginha.demo.web.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {
    public SseEmitter subscribe(Long memberId, String lastEventId);

    public void sendNotification(SseEmitter emitter, String emitterId, String eventId, Object data);

    public String createId(Long memberId);

    public void sendToMyAllEmitters(String memberId, String eventId, Object data);
}
