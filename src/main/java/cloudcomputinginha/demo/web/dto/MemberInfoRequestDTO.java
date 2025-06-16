package cloudcomputinginha.demo.web.dto;

import jakarta.validation.constraints.NotNull;
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
        @NotNull
        private String phone;
        @NotNull
        private String jobType;
        @NotNull
        private String introduction;
    }
}
