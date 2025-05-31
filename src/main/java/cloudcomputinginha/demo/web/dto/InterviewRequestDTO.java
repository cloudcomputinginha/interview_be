package cloudcomputinginha.demo.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class InterviewRequestDTO {
    @Getter
    public static class endInterviewRequestDTO {
        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime endedAt;
    }
}
