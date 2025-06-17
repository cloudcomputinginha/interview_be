package cloudcomputinginha.demo.web.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {
    public SseEmitter subscribe(Long memberId, String lastEventId);

    public String createId(Long memberId);

    public void sendToMyAllEmitters(Long memberId, String eventId, Object data);
}
