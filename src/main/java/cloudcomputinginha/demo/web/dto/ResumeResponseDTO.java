package cloudcomputinginha.demo.web.dto;

import lombok.*;

import java.time.LocalDateTime;

public class ResumeResponseDTO {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PresignedUploadDTO {
        private String presignedUrl;
        private String key;
        private String fileUrl;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CreateResumeResultDTO {
        private Long resumeId;
        private LocalDateTime createdAt;
    }
}
