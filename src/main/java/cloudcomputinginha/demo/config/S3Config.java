package cloudcomputinginha.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

// EC2의 Instance Metadata Service (IMDS)에서 IAM Role의 자격 증명을 가져옴.
// => EC2 인스턴스의 IAM Role을 사용하여 자동 인증
@Configuration
public class S3Config {
    public static String BUCKET_NAME;
    public static String REGION;

    private String region;
    private String authType;
    private String bucketName;

    // 생성자 주입을 통해 빈 생성될 때, 모든 값이 확실히 초기화되도록 보장
    public S3Config(@Value("${cloud.aws.s3.region}") String region,
                    @Value("${cloud.aws.s3.bucket}") String bucketName,
                    @Value("${cloud.aws.auth}") String authType) {
        this.region = region;
        this.authType = authType;
        this.bucketName = bucketName;

        // 다른 클래스에서 접근할 수 있도록 static 필드에 값 할당
        S3Config.REGION = region;
        S3Config.BUCKET_NAME = bucketName;
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }
}
