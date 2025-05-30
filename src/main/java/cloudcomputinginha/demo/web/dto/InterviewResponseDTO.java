package cloudcomputinginha.demo.web.dto;

import lombok.*;

import java.time.LocalDateTime;

public class InterviewResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InterviewEndResultDTO {
        private Long interviewId;
        private LocalDateTime endedAt;
    }
}
