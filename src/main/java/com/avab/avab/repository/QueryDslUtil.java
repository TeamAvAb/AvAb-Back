package com.avab.avab.repository;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;

public class QueryDslUtil {
    public static List<OrderSpecifier> orderByFromSort(Sort sort, EntityPathBase<?> parent) {
        if (sort == null) {
            return null;
        }

        return sort.stream()
                .map(
                        order -> {
                            Path<?> fieldPath =
                                    Expressions.path(Object.class, parent, order.getProperty());
                            return new OrderSpecifier(
                                    order.isAscending() ? Order.ASC : Order.DESC, fieldPath);
                        })
                .toList();
    }
}
