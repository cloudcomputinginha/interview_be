package cloudcomputinginha.demo.web.dto;

import cloudcomputinginha.demo.validation.annotation.ExistMember;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

public class ResumeRequestDTO {
    @Getter
    public static class ResumeCreateDTO {
        @ExistMember
        Long memberId; //TODO

        @NotEmpty
        String fileName;
        @NotEmpty
        String fileUrl;
        @NotNull
        @Positive
        Long fileSize;

    }
}
