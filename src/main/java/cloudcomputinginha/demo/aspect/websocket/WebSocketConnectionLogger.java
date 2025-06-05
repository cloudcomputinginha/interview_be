package cloudcomputinginha.demo.aspect.websocket;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;

@Aspect
@Slf4j
@Component
public class WebSocketConnectionLogger {

    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {
        log.info("🟢 WebSocket 연결됨: {}", event.getUser());
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        log.info("🔴 WebSocket 연결 해제: {}, 세션 ID: {}", event.getUser(), event.getSessionId());
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        log.info("📡 구독 요청: {}, 세션 ID: {}", event.getUser(), event.getMessage().getHeaders().get("simpSessionId"));
    }

    @EventListener
    public void handleUnsubscribeEvent(SessionUnsubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("📴 구독 취소: {}, 세션 ID: {}", event.getUser(), accessor.getSessionId());
    }
}