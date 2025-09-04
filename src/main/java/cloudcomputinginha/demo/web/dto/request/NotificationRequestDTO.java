package cloudcomputinginha.demo.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class NotificationRequestDTO {
    @Getter
    public static class FeedbackArrivedDTO {
        @NotNull
        private Long interviewId;
        @NotNull
        private Long memberInterviewId;
    }
}
