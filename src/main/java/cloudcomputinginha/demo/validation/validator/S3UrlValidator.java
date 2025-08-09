package cloudcomputinginha.demo.validation.validator;

import cloudcomputinginha.demo.config.S3Config;
import cloudcomputinginha.demo.validation.annotation.ValidS3Url;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class S3UrlValidator implements ConstraintValidator<ValidS3Url, String> {
    private static final String S3_URL_PREFIX = "https://" + S3Config.BUCKET_NAME + ".s3." + S3Config.REGION + ".amazonaws.com/";
    private static final Pattern S3_URL_PATTERN = Pattern.compile("^" + Pattern.quote(S3_URL_PREFIX) + ".*$");

    @Override
    public void initialize(ValidS3Url constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        if (url == null || url.isBlank()) {
            return false; // 빈 값은 유효하지 않다고 판단
        }
        return S3_URL_PATTERN.matcher(url).matches();
    }
}