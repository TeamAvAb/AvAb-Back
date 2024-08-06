package com.avab.avab.controller.enums;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SortCondition {
    RECENT("recent"),
    VIEW("view"),
    LIKE("like"),
    SCRAP("scrap");

    private final String value;

    private static final Map<String, SortCondition> sortConditionMap =
            Collections.unmodifiableMap(
                    Stream.of(values())
                            .collect(
                                    Collectors.toMap(
                                            SortCondition::getValue, Function.identity())));

    public static Optional<SortCondition> of(String value) {
        return Optional.ofNullable(sortConditionMap.get(value));
    }
}
