package com.avab.avab.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.avab.avab.validation.validator.ExistRecreationReviewValidator;

@Constraint(validatedBy = ExistRecreationReviewValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistRecreationReview {

    String message() default "존재하지 않는 리뷰입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
