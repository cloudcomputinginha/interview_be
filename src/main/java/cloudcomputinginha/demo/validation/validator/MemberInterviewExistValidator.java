package cloudcomputinginha.demo.validation.validator;

import cloudcomputinginha.demo.apiPayload.code.status.ErrorStatus;
import cloudcomputinginha.demo.repository.MemberInterviewRepository;
import cloudcomputinginha.demo.validation.annotation.ExistMemberInterview;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberInterviewExistValidator implements ConstraintValidator<ExistMemberInterview, Long> {
    private final MemberInterviewRepository memberInterviewRepository;

    @Override
    public void initialize(ExistMemberInterview constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = memberInterviewRepository.existsById(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.MEMBER_INTERVIEW_NOT_FOUND.toString()).addConstraintViolation();
        }

        return isValid;
    }
}
