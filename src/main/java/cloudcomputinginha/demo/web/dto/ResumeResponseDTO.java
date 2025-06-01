package cloudcomputinginha.demo.web.dto;

import cloudcomputinginha.demo.domain.enums.FileType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ResumeDTO {
        private Long resumeId;
        private String fileName;
        private Long fileSize;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ResumeDetailDTO {
        private Long resumeId;
        private String fileName;
        private String fileUrl;
        private Long fileSize;
        private FileType fileType;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ResumeListDTO {
        private List<ResumeDTO> resumes;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ResumeSimpleDTO {
        private Long resumeId;
        private String fileUrl;
    }
}
