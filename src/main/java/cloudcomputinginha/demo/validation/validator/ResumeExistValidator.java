package cloudcomputinginha.demo.validation.validator;


import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.repository.ResumeRepository;
import cloudcomputinginha.demo.validation.annotation.ExistResume;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResumeExistValidator implements ConstraintValidator<ExistResume, Long> {
    private final ResumeRepository resumeRepository;

    @Override
    public void initialize(ExistResume constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = resumeRepository.existsById(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.RESUME_NOT_FOUND.name()).addConstraintViolation();
        }

        return isValid;
    }
}
