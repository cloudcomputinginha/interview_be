package cloudcomputinginha.demo.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoRequestDTO {
    @NotNull
    private String phone;
    @NotNull
    private String jobType;
    @NotNull
    private String introduction;
}
