package com.edu.EvaluationWeb.component.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinLengthConstraints.class)
public @interface MinLength {
    int length() default 1;
    String message() default "Please, reach minimal required length";
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload() default {};
}
