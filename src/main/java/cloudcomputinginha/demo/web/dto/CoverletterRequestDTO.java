package cloudcomputinginha.demo.web.dto;

import cloudcomputinginha.demo.validation.annotation.ExistMember;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

public class CoverletterRequestDTO {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class createCoverletterDTO {
        @NotBlank
        @Size(max = 100)
        private String corporateName;

        @NotBlank
        @Size(max = 20)
        private String jobName;

        @NotEmpty
        @Valid
        private List<QnaRequestDTO.createQnaDTO> qnaDTOList;
    }
}
