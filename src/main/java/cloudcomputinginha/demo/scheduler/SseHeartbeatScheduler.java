package cloudcomputinginha.demo.scheduler;

import cloudcomputinginha.demo.web.sse.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SseHeartbeatScheduler {
    private final SseEmitterRepository sseEmitterRepository;

    @Scheduled(fixedRate = 35000) //35초마다 호출
    public void sendHeartbeat() {
        Map<String, SseEmitter> allEmitters = sseEmitterRepository.findAllEmitters();

        if (allEmitters == null || allEmitters.isEmpty()) {
            return;
        }

        for (Map.Entry<String, SseEmitter> entry : allEmitters.entrySet()) {
            String sseEmitterId = entry.getKey();
            SseEmitter sseEmitter = entry.getValue();

            try {
                sseEmitter.send(SseEmitter.event().name("heartbeat").data("ping"));
            } catch (IllegalStateException e) {
                sseEmitterRepository.deleteById(sseEmitterId);
            } catch (IOException e) {
                sseEmitterRepository.deleteById(sseEmitterId);
            }
        }
    }
}
