package cloudcomputinginha.demo.aspect.websocket;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;

@Slf4j
@Component
public class WebSocketConnectionLogger {

    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {
        log.info("🟢 WebSocket 연결됨: {}", event != null ? event.getSource() : "unknown");
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        log.info("🔴 WebSocket 연결 해제: {}, 세션 ID: {}",
            event != null ? event.getUser() : "unknown",
            event != null ? event.getSessionId() : "unknown");
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        if (event != null) {
            StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
            log.info("📡 구독 요청: {}, 세션 ID: {}", event.getUser(), accessor.getSessionId());
        }
    }

    @EventListener
    public void handleUnsubscribeEvent(SessionUnsubscribeEvent event) {
        if (event != null) {
            StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
            log.info("📴 구독 취소: {}, 세션 ID: {}", event.getUser(), accessor.getSessionId());
        }
    }
}