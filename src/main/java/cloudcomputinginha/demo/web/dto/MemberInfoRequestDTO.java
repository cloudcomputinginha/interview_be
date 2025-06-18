package cloudcomputinginha.demo.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberInfoRequestDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class registerInfoDTO {
        @Schema(description = "휴대폰 번호", example = "010-1234-5678")
        @Pattern(
                regexp = "^01[016789]-\\d{4}-\\d{4}$",
                message = "올바른 휴대폰 번호 형식이 아닙니다. 예: 010-1234-5678"
        )
        @NotNull
        private String phone;
        @NotNull
        private String jobType;
        @NotNull
        private String introduction;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class updateInfoDTO {
        @NotNull
        private String name;
        @Schema(description = "휴대폰 번호", example = "010-1234-5678")
        @Pattern(
                regexp = "^01[016789]-\\d{4}-\\d{4}$",
                message = "올바른 휴대폰 번호 형식이 아닙니다. 예: 010-1234-5678"
        )
        @NotNull
        private String phone;
        @NotNull
        private String jobType;
        @NotNull
        private String introduction;
    }
}
