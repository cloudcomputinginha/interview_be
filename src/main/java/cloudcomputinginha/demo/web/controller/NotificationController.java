package cloudcomputinginha.demo.web.controller;

import cloudcomputinginha.demo.apiPayload.ApiResponse;
import cloudcomputinginha.demo.preprocessor.NotificationPreprocessor;
import cloudcomputinginha.demo.web.dto.NotificationRequestDTO;
import cloudcomputinginha.demo.web.sse.SseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "알림 API")
@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final SseService notificationSseService;
    private final NotificationPreprocessor notificationPreprocessor;

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
}
