package cloudcomputinginha.demo.web.sse.event;

import cloudcomputinginha.demo.web.dto.NotificationResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NotificationEvent {
    private final String receiverId;
    private final String eventId;
    private final NotificationResponseDTO.NotificationDTO notificationDTO;
}
