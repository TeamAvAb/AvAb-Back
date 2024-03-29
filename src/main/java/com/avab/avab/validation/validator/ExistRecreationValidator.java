package com.avab.avab.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.repository.RecreationRepository;
import com.avab.avab.validation.annotation.ExistRecreation;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ExistRecreationValidator implements ConstraintValidator<ExistRecreation, Long> {
    private final RecreationRepository recreationRepository;

    @Override
    public void initialize(ExistRecreation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = recreationRepository.existsById(value);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            ErrorStatus.RECREATION_NOT_FOUND.toString())
                    .addConstraintViolation();
        }
        return isValid;
    }
}
