package cloudcomputinginha.demo.infra.aws;

import cloudcomputinginha.demo.apiPayload.code.handler.ResumeHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.config.properties.CloudProperties;
import cloudcomputinginha.demo.web.dto.ResumeResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeS3Service {
    private final S3Presigner s3Presigner;
    private final CloudProperties cloudProperties;

    public ResumeResponseDTO.PresignedUploadDTO getUploadPresignedURL(String fileName) {
        if (!fileName.toLowerCase().endsWith(".pdf")) {
            throw new ResumeHandler(ErrorStatus.RESUME_FILE_TYPE_INVALID);
        }

        String key = "resumes/" + UUID.randomUUID();
        String fileUrl = "https://" + cloudProperties.getS3().getBucket() + ".s3." + cloudProperties.getS3().getRegion() + ".amazonaws.com/" + key;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(cloudProperties.getS3().getBucket())
                .key(key)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(
                b -> b.signatureDuration(Duration.ofMinutes(5))
                        .putObjectRequest(objectRequest));

        return new ResumeResponseDTO.PresignedUploadDTO(presignedRequest.url().toString(), key, fileUrl);
    }
}