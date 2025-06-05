package cloudcomputinginha.demo.web.controller;

import cloudcomputinginha.demo.apiPayload.exception.GeneralException;
import cloudcomputinginha.demo.infra.messaging.WaitingRoomEventPublisher;
import cloudcomputinginha.demo.service.MemberInterviewSocketService;
import cloudcomputinginha.demo.web.dto.MemberInterviewSocketMessageDTO.WaitingRoomActionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import cloudcomputinginha.demo.web.session.InterviewWaitingRoomSessionManager;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberInterviewSocketController {

    private final InterviewWaitingRoomSessionManager sessionManager;
    private final WaitingRoomEventPublisher eventPublisher;
    private final MemberInterviewSocketService memberInterviewSocketService;

    // 2025.06.05
    // 작성자 : 박준혁
    // 예외 처리
    // 일반 HTTP의 예외가 @ControllerAdvice에서 잡아준다면,
    // @MessageMapping 내부에서 동작하는 비즈니스 로직은, WebSocketConfig 내의 StompSubProtocolErrorHandler가 잡아서 처리
    // 따라서 기존 HTTP API 처리하는 거처럼 예외 처리하시면 됩니다.

    @MessageMapping("/waiting-room/enter")
    public void enter(@Payload WaitingRoomActionDTO waitingRoomActionDTO, SimpMessageHeaderAccessor accessor) {

        memberInterviewSocketService.enterWaitingRoom(
            waitingRoomActionDTO, accessor.getSessionId());

    }

    // 명시적인 퇴장일 경우
    @MessageMapping("/waiting-room/leave")
    public void leave(@Payload WaitingRoomActionDTO leaveDTO, SimpMessageHeaderAccessor accessor) {

        memberInterviewSocketService.leaveWaitingRoom(leaveDTO.getInterviewId(), leaveDTO.getMemberId(), accessor.getSessionId());
    }

    // 갑작스런 퇴장일 경우
    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String sessionId = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
        // 세션 제거
        var sessionInfo = sessionManager.remove(sessionId);
        if (sessionInfo == null) return; // 이미 제거된 세션

        memberInterviewSocketService.leaveWaitingRoom(
            sessionInfo.interviewId(),
            sessionInfo.memberId(),
            StompHeaderAccessor.wrap(event.getMessage()).getSessionId());

        // 참가자 목록 갱신
        eventPublisher.broadcastParticipants(sessionInfo.interviewId());
    }

    @MessageExceptionHandler(GeneralException.class)
    @SendToUser("/queue/errors")
    public String handleGeneralException(GeneralException ex) {
        return ex.getErrorReason().getMessage();
    }

}
