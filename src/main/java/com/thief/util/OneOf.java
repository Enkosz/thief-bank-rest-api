package com.thief.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = OneOfValidator.class)
public @interface OneOf {
    String message() default "Exactly one of the fields must be set and the other must be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Fields to validate against null.
     */
    String[] fields() default {};
}
