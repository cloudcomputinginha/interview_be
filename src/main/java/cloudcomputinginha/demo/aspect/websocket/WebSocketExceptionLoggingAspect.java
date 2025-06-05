package cloudcomputinginha.demo.aspect.websocket;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class WebSocketExceptionLoggingAspect {

    @Pointcut("@annotation(org.springframework.messaging.handler.annotation.MessageMapping)")
    public void messageMappingPointcut() {}

    @AfterThrowing(pointcut = "messageMappingPointcut()", throwing = "ex")
    public void logWebSocketException(JoinPoint joinPoint, Throwable ex) {
        log.error("💥 WebSocket 예외 발생: {}.{}() - {}",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            ex.getMessage(), ex);
    }
}
