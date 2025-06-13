package cloudcomputinginha.demo.aspect.websocket;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class WebSocketMessageLoggingAspect {

    @Pointcut("@annotation(messageMapping)")
    public void messageMappingMethods(MessageMapping messageMapping) {}

    @Around("messageMappingMethods(messageMapping)")
    public Object logStompMessage(ProceedingJoinPoint joinPoint, MessageMapping messageMapping) throws Throwable {
        String destination = messageMapping.value().length > 0 ? messageMapping.value()[0] : "(unknown)";
        log.info("📩 WebSocket 수신 - Destination: {}, 메서드: {}.{}()",
            destination,
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName());

        Object result = joinPoint.proceed();

        log.info("✅ WebSocket 처리 완료 - Destination: {}, 결과 타입: {}",
            destination, result != null ? result.getClass().getSimpleName() : "null");
        return result;
    }
}