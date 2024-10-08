package com.avab.avab.repository;

import static com.avab.avab.domain.QFlow.flow;
import static com.avab.avab.domain.QRecreation.recreation;
import static com.avab.avab.domain.QReport.report;

import com.avab.avab.domain.QFlow;
import com.avab.avab.domain.QRecreation;
import com.avab.avab.domain.QRecreationReview;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.ReportType;
import com.avab.avab.domain.enums.UserStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;

class MaskingPredicates {
    static BooleanExpression mask(QFlow flow, User user) {
        return notSoftDeleted(flow).and(notReportedByUser(flow, user)).and(notAuthorDeleted(flow));
    }

    static BooleanExpression mask(QRecreation recreation, User user) {
        return notSoftDeleted(recreation)
                .and(notReportedByUser(recreation, user))
                .and(notAuthorDeleted(recreation));
    }

    static BooleanExpression mask(QRecreationReview recreationReview, User user) {
        return notSoftDeleted(recreationReview)
                .and(notReportedByUser(recreationReview, user))
                .and(notAuthorDeleted(recreationReview));
    }

    static BooleanExpression notSoftDeleted(QRecreationReview recreationReview) {
        return recreationReview.deletedAt.isNull();
    }

    static BooleanExpression notSoftDeleted(QFlow flow) {
        return flow.deletedAt.isNull();
    }

    static BooleanExpression notSoftDeleted(QRecreation recreation) {
        return recreation.deletedAt.isNull();
    }

    static BooleanExpression notReportedByUser(QFlow flow, User user) {
        if (user == null) {
            return null;
        }

        return flow.id.notIn(
                JPAExpressions.select(report.targetFlow.id)
                        .from(report)
                        .where(report.reportType.eq(ReportType.FLOW), report.reporter.eq(user)));
    }

    static BooleanExpression notReportedByUser(QRecreation recreation, User user) {
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

    static BooleanExpression notReportedByUser(QRecreationReview recreationReview, User user) {
        if (user == null) {
            return null;
        }

        return recreationReview.id.notIn(
                JPAExpressions.select(report.targetRecreationReview.id)
                        .from(report)
                        .where(
                                report.reportType.eq(ReportType.RECREATION_REVIEW),
                                report.reporter.eq(user)));
    }

    static BooleanExpression notAuthorDeleted(QFlow flow) {
        return flow.author.userStatus.ne(UserStatus.DELETED);
    }

    static BooleanExpression notAuthorDeleted(QRecreation recreation) {
        return recreation.author.userStatus.ne(UserStatus.DELETED);
    }

    static BooleanExpression notAuthorDeleted(QRecreationReview recreationReview) {
        return recreationReview.author.userStatus.ne(UserStatus.DELETED);
    }
}
