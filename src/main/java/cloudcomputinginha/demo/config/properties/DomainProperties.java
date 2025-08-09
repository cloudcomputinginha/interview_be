package cloudcomputinginha.demo.config.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "domain")
public class DomainProperties {
    @NotBlank(message = "백엔드 도메인은 필수입니다.")
    private String backend;

    @NotBlank(message = "AI 도메인은 필수입니다.")
    private String ai;
}
