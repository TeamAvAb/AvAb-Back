package com.avab.avab.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.service.FlowService;
import com.avab.avab.validation.annotation.ExistFlow;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExistFlowValidator implements ConstraintValidator<ExistFlow, Long> {

    private final FlowService flowService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = flowService.existsByFlowId(value);

        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(ErrorStatus.FLOW_NOT_FOUND.toString())
                    .addConstraintViolation();
        }

        return isValid;
    }
}
