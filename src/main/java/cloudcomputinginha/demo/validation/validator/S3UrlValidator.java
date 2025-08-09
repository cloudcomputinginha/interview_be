package cloudcomputinginha.demo.validation.validator;

import cloudcomputinginha.demo.config.properties.CloudProperties;
import cloudcomputinginha.demo.validation.annotation.ValidS3Url;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class S3UrlValidator implements ConstraintValidator<ValidS3Url, String> {
    private final CloudProperties cloudProperties;
    private Pattern s3UrlPattern;

    public S3UrlValidator(CloudProperties cloudProperties) {
        this.cloudProperties = cloudProperties;

        String bucketName = cloudProperties.getS3().getBucket();
        String region = cloudProperties.getS3().getRegion();
        String s3UrlPrefix = "https://" + bucketName + ".s3." + region + ".amazonaws.com/";
        this.s3UrlPattern = Pattern.compile("^" + Pattern.quote(s3UrlPrefix) + ".*$");
    }

    @Override
    public void initialize(ValidS3Url constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        if (url == null || url.isBlank()) {
            return false; // 빈 값은 유효하지 않다고 판단
        }
        return s3UrlPattern.matcher(url).matches();
    }
}