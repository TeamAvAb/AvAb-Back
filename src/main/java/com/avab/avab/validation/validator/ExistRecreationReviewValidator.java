package com.avab.avab.validation.validator;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.service.RecreationReviewService;
import com.avab.avab.validation.annotation.ExistRecreationReview;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistRecreationReviewValidator
    implements ConstraintValidator<ExistRecreationReview, Long> {

    private final RecreationReviewService recreationReviewService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = recreationReviewService.existsById(value);

        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                .buildConstraintViolationWithTemplate(
                    ErrorStatus.REVIEW_NOT_FOUND.toString())
                .addConstraintViolation();
        }

        return isValid;
    }
}
