package com.avab.avab.repository;

import static com.avab.avab.domain.QFlow.flow;
import static com.avab.avab.domain.QRecreation.recreation;
import static com.avab.avab.domain.QReport.report;

import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.ReportType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;

class MaskingPredicates {

    static BooleanExpression notSoftDeletedFlow() {
        return flow.deletedAt.isNull();
    }

    static BooleanExpression notSoftDeletedRecreation() {
        return recreation.deletedAt.isNull();
    }

    static BooleanExpression notReportedFlowByUser(User user) {
        if (user == null) {
            return null;
        }

        return flow.id.notIn(
                JPAExpressions.select(report.targetFlow.id)
                        .from(report)
                        .where(report.reportType.eq(ReportType.FLOW), report.reporter.eq(user)));
    }

    static BooleanExpression notReportedRecreationByUser(User user) {
        if (user == null) {
            return null;
        }

        return recreation.id.notIn(
                JPAExpressions.select(report.targetRecreation.id)
                        .from(report)
                        .where(
                                report.reportType.eq(ReportType.RECREATION),
                                report.reporter.eq(user)));
    }
}
