package cloudcomputinginha.demo.web.controller;

import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.apiPayload.code.handler.MemberHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.converter.NotificationConverter;
import cloudcomputinginha.demo.domain.Notification;
import cloudcomputinginha.demo.preprocessor.NotificationPreprocessor;
import cloudcomputinginha.demo.repository.MemberRepository;
import cloudcomputinginha.demo.service.notification.NotificationCommandService;
import cloudcomputinginha.demo.service.notification.NotificationQueryService;
import cloudcomputinginha.demo.web.dto.NotificationRequestDTO;
import cloudcomputinginha.demo.web.dto.NotificationResponseDTO;
import cloudcomputinginha.demo.web.sse.SseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Tag(name = "알림 API")
@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final SseService notificationSseService;
    private final NotificationPreprocessor notificationPreprocessor;
    private final NotificationQueryService notificationQueryService;
    private final NotificationCommandService notificationCommandService;
    private final MemberRepository memberRepository;

    @Operation(summary = "SSE 알림 구독", description = "앞으로 서버로부터 실시간 알림을 수신합니다.")
    @GetMapping(value = "/notifications/subscribe", produces = "text/event-stream")
    public SseEmitter subscribeNotifications(@AuthenticationPrincipal Long memberId,
                                             @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationSseService.subscribe(memberId, lastEventId);
    }

    // jwt token 없어도 허용
    @PostMapping("/notifications/feedback")
    @Operation(summary = "전체 피드백 생성 완료 알림 전송", description = "AI 서버에서 전체 피드백 생성을 완료하면 모의 면접 참가자에게 알림을 전송합니다.")
    public ApiResponse<Void> notifyFeedbackCreated(@RequestBody @Valid NotificationRequestDTO.FeedbackArrivedDTO feedBackArrivedDTO) {
        notificationPreprocessor.preprocessFeedbackDTO(feedBackArrivedDTO);
        return ApiResponse.onSuccess(null);
    }

    @GetMapping("/notifications")
    @Operation(summary = "한 달 간 받은 모든 알림 조회", description = "한 달 간 받은 모든 알림을 한 번에 조회합니다.(알림함 클릭할 때 요청)")
    public ApiResponse<NotificationResponseDTO.NotificationListDTO> findNotifications(@AuthenticationPrincipal Long memberId) {
        // TODO: member 인증 과정 validator나 AOP 일괄처리
        if (!memberRepository.existsById(memberId)) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }

        List<Notification> recentNotifications = notificationQueryService.findRecentNotifications(memberId);
        return ApiResponse.onSuccess(NotificationConverter.toNotificationListDTO(recentNotifications));
    }

    @PatchMapping("/notifications/{notificationId}/read")
    @Operation(summary = "단일 알림 읽음 처리", description = "알림을 읽음 처리합니다.")
    public ApiResponse<Void> readNotification(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long notificationId) {
        // TODO: member 인증 과정 validator나 AOP 일괄처리
        if (!memberRepository.existsById(memberId)) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }
        notificationCommandService.markAsRead(memberId, notificationId);
        return ApiResponse.onSuccess(null);
    }
}
