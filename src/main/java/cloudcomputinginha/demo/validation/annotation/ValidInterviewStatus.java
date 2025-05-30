package cloudcomputinginha.demo.validation.annotation;

import cloudcomputinginha.demo.validation.validator.InterviewStatusValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InterviewStatusValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidInterviewStatus {
    String message() default "유효하지 않은 사용자의 인터뷰 상태입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
