package com.avab.avab.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.validation.annotation.ValidatePage;

public class PageValidator implements ConstraintValidator<ValidatePage, Integer> {

    @Override
    public void initialize(ValidatePage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = value >= 0;

        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(ErrorStatus.PAGE_UNDER_ZERO.toString())
                    .addConstraintViolation();
        }

        return isValid;
    }
}
