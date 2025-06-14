package cloudcomputinginha.demo.web.controller;

import cloudcomputinginha.demo.service.notification.NotificationSseService;
import cloudcomputinginha.demo.validation.annotation.ExistMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "알림 API")
@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationSseService notificationSseService;

    @Operation(summary = "SSE 알림 구독", description = "앞으로 서버로부터 실시간 알림을 수신합니다.")
    @GetMapping(value = "/notifications/subscribe", produces = "text/event-stream")
    public SseEmitter subscribeNotifications(@RequestParam @ExistMember @NotNull Long memberId,
                                             @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationSseService.subscribe(memberId, lastEventId);
    }
}
