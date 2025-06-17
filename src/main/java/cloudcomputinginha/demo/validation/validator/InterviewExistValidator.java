package cloudcomputinginha.demo.validation.validator;

import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.repository.InterviewRepository;
import cloudcomputinginha.demo.validation.annotation.ExistInterview;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InterviewExistValidator implements ConstraintValidator<ExistInterview, Long> {
    private final InterviewRepository interviewRepository;

    @Override
    public void initialize(ExistInterview constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = interviewRepository.existsById(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.INTERVIEW_NOT_FOUND.name()).addConstraintViolation();
        }

        return isValid;
    }
}
