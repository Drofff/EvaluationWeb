package com.drofff.edu.component.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;

public class DurationConstraints implements ConstraintValidator<LimitedDuration, Duration> {

    private Integer maxMinutes;

    @Override
    public void initialize(LimitedDuration constraintAnnotation) {
        this.maxMinutes = constraintAnnotation.maxMinutes();
    }

    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext constraintValidatorContext) {
        Long minutes = duration.toMinutes();
        return minutes > 0 && minutes <= maxMinutes;
    }

}
