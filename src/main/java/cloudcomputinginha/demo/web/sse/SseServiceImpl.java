package cloudcomputinginha.demo.web.sse;

import cloudcomputinginha.demo.apiPayload.code.handler.MemberHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.NotificationHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.repository.MemberRepository;
import cloudcomputinginha.demo.service.notification.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SseServiceImpl implements SseService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; //1H
    private final SseEmitterRepository sseEmitterRepository;
    private final MemberRepository memberRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleCreateNotification(NotificationEvent event) {
        sendToMyAllEmitters(event.getReceiverId(), event.getEventId(), event.getNotificationDTO());
    }

    @Override
    public SseEmitter subscribe(Long memberId, String lastEventId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 고유한 SseEmitter 생성
        String emitterId = createId(memberId);
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        sseEmitterRepository.save(emitterId, emitter);

        // 시간 초과나 비동기 요청이 안되면 자동으로 삭제
        emitter.onCompletion(() -> sseEmitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> sseEmitterRepository.deleteById(emitterId));

        // 503 에러 방지를 위한 더미 데이터 전송
        String eventId = createId(memberId);
        sendNotification(emitter, emitterId, eventId, "EventStream created. [userId=" + memberId + "]");

        // 받지 못한 데이터가 존재한다면 Last-Event-ID를 기준으로 그 뒤 이벤트를 알림으로 전송
        if (!lastEventId.isBlank()) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }
        return emitter;
    }

    private void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = sseEmitterRepository.findAllEventCacheStartWithMemberId(String.valueOf(memberId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    @Override
    // SseEmitter가 연결된 클라이언트에 data(string 또는 Notification)을 전송
    public void sendNotification(SseEmitter emitter, String emitterId, String eventId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            sseEmitterRepository.deleteById(emitterId);
            throw new NotificationHandler(ErrorStatus.NOTIFICATION_SEND_FAIL);
        }
    }

    @Override
    public String createId(Long memberId) {
        return memberId + "_" + System.currentTimeMillis();
    }

    @Override
    public void sendToMyAllEmitters(String memberId, String eventId, Object data) {
        Map<String, SseEmitter> emitters = sseEmitterRepository.findAllEmitterStartWithMemberId(memberId);
        sseEmitterRepository.saveEventCache(eventId, data);
        emitters.forEach(
                (emitterId, emitter) -> {
                    sendNotification(emitter, emitterId, eventId, data);
                }
        );
    }
}
