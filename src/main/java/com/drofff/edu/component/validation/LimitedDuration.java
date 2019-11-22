package com.drofff.edu.component.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = DurationConstraints.class)
public @interface LimitedDuration {

    Class<?> [] groups() default {};

    Class<? extends Payload> [] payload() default {};

    String message() default "Error: specified value is grater than maximum duration";

    int maxMinutes() default 70;

}
