package cloudcomputinginha.demo.web.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface SseEmitterRepository {
    Map<String, SseEmitter> findAllEmitters();

    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    void saveEventCache(String eventId, Object event);

    Map<String, SseEmitter> findAllEmitterStartWithMemberId(Long memberId);

    Map<String, Object> findAllEventCacheStartWithMemberId(String memberId);

    void deleteById(String emitterId);

    void deleteAllEmitterStartWithId(String memberId);

    void deleteAllEventCacheStartWithId(String memberId);
}
