package cloudcomputinginha.demo.validation.annotation;

import cloudcomputinginha.demo.validation.validator.FileNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileNameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFileName {
    String message() default "유효하지 않은 파일 이름입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

