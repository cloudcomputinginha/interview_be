package cloudcomputinginha.demo.validation.annotation;

import cloudcomputinginha.demo.validation.validator.S3UrlValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = S3UrlValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidS3Url {
    String message() default "유효하지 않은 S3 객체 URL 형식입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}