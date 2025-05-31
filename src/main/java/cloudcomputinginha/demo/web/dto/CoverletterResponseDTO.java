package cloudcomputinginha.demo.web.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class CoverletterResponseDTO {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class createCoverletterDTO {
        private Long coverletterId;
        private LocalDateTime createdAt;
    }


    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MyCoverletterListDTO {
        private List<MyCoverletterDTO> coverletters;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MyCoverletterDTO {
        private Long coverletterId;
        private String corporateName;
        private String jobName;
        private LocalDateTime createdAt;
    }
}
