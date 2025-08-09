package cloudcomputinginha.demo.config;

import cloudcomputinginha.demo.config.properties.CloudProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

// EC2의 Instance Metadata Service (IMDS)에서 IAM Role의 자격 증명을 가져옴.
// => EC2 인스턴스의 IAM Role을 사용하여 자동 인증
@Configuration
@RequiredArgsConstructor
public class S3Config {
    private final CloudProperties cloudProperties;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(cloudProperties.getS3().getRegion())) // 변경된 부분
                .credentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(cloudProperties.getS3().getRegion())) // 변경된 부분
                .credentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }
}
