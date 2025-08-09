package cloudcomputinginha.demo.web.dto;

import cloudcomputinginha.demo.validation.annotation.ValidS3Url;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

public class ResumeRequestDTO {
    @Getter
    public static class ResumeCreateDTO {
        @NotEmpty
        String fileName;
        @NotEmpty
        @ValidS3Url
        String fileUrl;
        @NotNull
        @Positive
        Long fileSize;

    }
}
