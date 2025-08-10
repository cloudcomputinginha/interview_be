package cloudcomputinginha.demo.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "frontend")
@Validated
public class FrontendRedirectProperties {
    @NotBlank(message = "기본 리다이렉트 URI는 필수입니다.")
    private String defaultRedirectUri;
    @NotNull(message = "허용되는 리다이렉트 URI 목록은 필수입니다.")
    private List<String> allowedRedirectUris;
}
