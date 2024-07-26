package com.avab.avab.controller.handler.resolver;

import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.GeneralException;
import com.avab.avab.controller.enums.SortCondition;
import com.avab.avab.controller.handler.annotation.ParseSortCondition;

@Component
public class ParseSortConditionArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ParseSortCondition.class);
    }

    @Override
    public SortCondition resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory)
            throws Exception {
        String sortByValue = webRequest.getParameter("sortBy");

        Optional<SortCondition> sortCondition = SortCondition.of(sortByValue);
        if (sortCondition.isEmpty()) {
            throw new GeneralException(ErrorStatus.INVALID_SORT_CONDITION);
        }

        return sortCondition.get();
    }
}
