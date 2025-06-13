package cloudcomputinginha.demo.apiPayload.code.handler;

import cloudcomputinginha.demo.apiPayload.code.ErrorReasonDTO;
import cloudcomputinginha.demo.apiPayload.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Slf4j
@Component
public class SocketErrorHandler extends StompSubProtocolErrorHandler {

    public SocketErrorHandler() {
        super();
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        String message;
        String errorCode = "UNKNOWN";

        if (ex instanceof GeneralException ge) {
            log.error("GeneralException", ge);
            ErrorReasonDTO reason = ge.getErrorReason();
            message = reason.getMessage();
            errorCode = reason.getCode();
        } else {
            message = "WebSocket 처리 중 오류가 발생했습니다.";
        }

        log.warn("STOMP Error: [{}] {}", errorCode, message);

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setMessage(message);
        accessor.setLeaveMutable(true);
        accessor.addNativeHeader("error-code", errorCode);

        return MessageBuilder.createMessage(message.getBytes(), accessor.getMessageHeaders());
    }
}