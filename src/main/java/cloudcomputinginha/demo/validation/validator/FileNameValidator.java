package cloudcomputinginha.demo.validation.validator;

import cloudcomputinginha.demo.validation.annotation.ValidFileName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class FileNameValidator implements ConstraintValidator<ValidFileName, String> {

    //파일 이름은 한글, 영문, 숫자, 언더바(_), 대시(-), 공백만 허용하며 .pdf로 끝나야 합니다.
    private static final Pattern FILE_PATTERN = Pattern.compile("^[가-힣A-Za-z0-9_\\- ]+\\.pdf$");

    @Override
    public void initialize(ValidFileName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && FILE_PATTERN.matcher(value).matches();
    }
}
