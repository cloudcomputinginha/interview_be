package cloudcomputinginha.demo.validation.validator;

import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.repository.CoverletterRepository;
import cloudcomputinginha.demo.validation.annotation.ExistCoverletter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CoverletterExistValidator implements ConstraintValidator<ExistCoverletter, Long> {
    private final CoverletterRepository coverletterRepository;

    @Override
    public void initialize(ExistCoverletter constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = coverletterRepository.existsById(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.COVERLETTER_NOT_FOUND.name()).addConstraintViolation();
        }

        return isValid;
    }
}
