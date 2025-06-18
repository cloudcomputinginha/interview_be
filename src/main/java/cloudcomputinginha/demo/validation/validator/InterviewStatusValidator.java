package cloudcomputinginha.demo.validation.validator;

import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.domain.enums.InterviewStatus;
import cloudcomputinginha.demo.validation.annotation.ValidInterviewStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class InterviewStatusValidator implements ConstraintValidator<ValidInterviewStatus, String> {
    @Override
    public void initialize(ValidInterviewStatus constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = InterviewStatus.isValid(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.INTERVIEW_STATUS_INVALID.name()).addConstraintViolation();
        }

        return isValid;
    }
}
