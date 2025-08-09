package cloudcomputinginha.demo.config.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cloud.aws")
public class CloudProperties {

    @NotBlank(message = "AWS 인증 방식은 필수입니다.")
    private String auth;

    @Valid
    private S3 s3 = new S3();

    @Getter
    @Setter
    public static class S3 {
        @NotBlank(message = "S3 버킷 이름은 필수입니다.")
        private String bucket;

        @NotBlank(message = "AWS 리전은 필수입니다.")
        private String region;
    }
}
