package cloudcomputinginha.demo.service;

import cloudcomputinginha.demo.apiPayload.code.handler.ResumeHandler;
import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.web.dto.ResumeResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3PresignedService {
    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.region}")
    private String region;

    public ResumeResponseDTO.PresignedUploadDTO getUploadPresignedURL(String fileName) {
        if (!fileName.toLowerCase().endsWith(".pdf")) {
            throw new ResumeHandler(ErrorStatus.RESUME_FILE_TPYE_INVALID);
        }


        String key = "resumes/" + UUID.randomUUID() + "_" + fileName;
        String fileUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(
                req -> req.signatureDuration(Duration.ofMinutes(15))
                        .putObjectRequest(
                                PutObjectRequest.builder()
                                        .bucket(bucketName)
                                        .key(key)
                                        .contentType("application/pdf")
                                        .acl(ObjectCannedACL.PUBLIC_READ)
                                        .build()
                        )
        );

        return new ResumeResponseDTO.PresignedUploadDTO(presignedRequest.url().toString(), key, fileUrl);
    }
}