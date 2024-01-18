package com.avab.avab.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.avab.avab.validation.validator.ExistRecreationValidator;

@Documented
@Constraint(validatedBy = ExistRecreationValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistRecreation {
    String message() default "존재하지 않는 레크레이션입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
