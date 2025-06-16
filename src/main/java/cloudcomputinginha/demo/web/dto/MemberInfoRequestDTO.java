package cloudcomputinginha.demo.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoRequestDTO {
    @NotBlank
    private String phone;
    @NotBlank
    private String jobType;
    @NotBlank
    private String introduction;
}
