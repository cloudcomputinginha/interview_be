package cloudcomputinginha.demo.web.sse;

import cloudcomputinginha.demo.apiPayload.code.handler.MemberHandler;
import cloudcomputinginha.demo.apiPayload.code.handler.NotificationHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.converter.NotificationConverter;
import cloudcomputinginha.demo.domain.Member;
import cloudcomputinginha.demo.repository.MemberRepository;
import cloudcomputinginha.demo.repository.NotificationRepository;
import cloudcomputinginha.demo.web.dto.NotificationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SseServiceImpl implements SseService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; //1H
    private final SseEmitterRepository sseEmitterRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

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

    @Override
    /**
     * 현재 나와 연결된 모든 emitter에 data를 보냅니다. -> sendNotification 호출
     */
    public void sendToMyAllEmitters(Long memberId, String eventId, Object data) {
        Map<String, SseEmitter> emitters = sseEmitterRepository.findAllEmitterStartWithMemberId(memberId);
        sseEmitterRepository.saveEventCache(eventId, data);

        Object sseData = preprocessData(memberId, data);

        emitters.forEach(
                (emitterId, emitter) -> {
                    try {
                        sendNotification(emitter, emitterId, eventId, sseData);
                    } catch (NotificationHandler ignored) {
                        // 하나의 emitter가 실패하더라도 다른 emitter는 정상적으로 동작하게 한다.
                    }
                }
        );
    }

    @Override
    public String createId(Long memberId) {
        return memberId + "_" + System.currentTimeMillis();
    }

    /**
     * eventCache를 기반으로 놓친 data를 전송합니다. -> sendNotification 호출
     */
    private void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = sseEmitterRepository.findAllEventCacheStartWithMemberId(String.valueOf(memberId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> {
                    try {
                        sendNotification(emitter, emitterId, entry.getKey(), entry.getValue());
                    } catch (NotificationHandler ignored) {
                        // 하나의 emitter가 실패하더라도 다른 emitter는 정상적으로 동작하게 한다.
                    }
                });
    }

    /**
     * SseEmitter가 연결된 클라이언트에 data(string 또는 Notification)을 전송
     */
    private void sendNotification(SseEmitter emitter, String emitterId, String eventId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            sseEmitterRepository.deleteById(emitterId);
            throw new NotificationHandler(ErrorStatus.NOTIFICATION_SEND_FAIL);
        }
    }

    private Object preprocessData(Long memberId, Object data) {
        if (data instanceof NotificationResponseDTO.NotificationDTO) {
            Long unReadCount = notificationRepository.countUnread(memberId, LocalDateTime.now().minusMonths(1));
            return NotificationConverter.toNotificationSSEDTO((NotificationResponseDTO.NotificationDTO) data, unReadCount);
        }
        return data;
    }
}
