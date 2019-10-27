package com.edu.EvaluationWeb.component.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinLengthConstraints implements ConstraintValidator<MinLength, String> {

    int minLength;

    @Override
    public void initialize(MinLength constraintAnnotation) {
        minLength = constraintAnnotation.length();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.length() >= minLength;
    }

}
