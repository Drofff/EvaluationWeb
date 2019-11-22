package com.drofff.edu.component.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = DateTimeConstraints.class)
public @interface ReachableDate {
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};
    String message() default "Please, provide reachable date";
}
