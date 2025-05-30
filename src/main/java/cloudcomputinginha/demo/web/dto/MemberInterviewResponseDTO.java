package cloudcomputinginha.demo.web.dto;

import cloudcomputinginha.demo.domain.enums.InterviewStatus;
import lombok.*;

import java.time.LocalDateTime;

public class MemberInterviewResponseDTO {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MemberInterviewStatusDTO {
        Long memberInterviewId;
        InterviewStatus status;
        LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CreateMemberInterviewDTO {
        Long memberInterviewId;
        LocalDateTime createdAt;
    }
}
