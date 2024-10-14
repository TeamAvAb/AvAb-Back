package com.avab.avab.repository;

import static com.avab.avab.domain.QFlow.flow;
import static com.avab.avab.domain.QFlowAge.flowAge;
import static com.avab.avab.domain.QFlowGender.flowGender;
import static com.avab.avab.domain.mapping.QFlowRecreationKeyword.flowRecreationKeyword;
import static com.avab.avab.domain.mapping.QFlowRecreationPurpose.flowRecreationPurpose;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Triple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Purpose;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FlowCustomRepositoryImpl implements FlowCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Flow> recommendFlows(
            List<Keyword> keyword,
            Integer participant,
            Integer totalPlayTime,
            List<Purpose> purpose,
            List<Gender> gender,
            List<Age> age,
            User user) {
        ArrayList<Triple<Flow, Double, Double>> flowList = new ArrayList<>();
        List<Long> flows =
                queryFactory
                        .select(flow.id)
                        .from(flow)
                        .where(MaskingPredicates.mask(flow, user))
                        .fetch();

        for (Long flowId : flows) {
            Flow nowFlow = queryFactory.selectFrom(flow).where(flow.id.eq(flowId)).fetchOne();

            // 목적(필수) 비교
            List<Purpose> purposesForComparison =
                    queryFactory
                            .select(flowRecreationPurpose.purpose.purpose)
                            .from(flowRecreationPurpose)
                            .where(flowRecreationPurpose.flow.id.eq(flowId))
                            .fetch();

            long purposeMatchSize =
                    purposesForComparison.stream().filter(purpose::contains).count();

            // 시간(필수) 비교
            long loePlayTime =
                    nowFlow.getTotalPlayTime() <= totalPlayTime
                            ? -Math.abs(nowFlow.getTotalPlayTime() - totalPlayTime)
                            : -10000;

            // 겹치는 키워드 체크
            List<Keyword> keywordsForComparison =
                    queryFactory
                            .select(flowRecreationKeyword.keyword.keyword)
                            .from(flowRecreationKeyword)
                            .where(flowRecreationKeyword.flow.id.eq(flowId))
                            .fetch();

            long keywordMatchSize =
                    keyword != null
                            ? keyword.stream().filter(keywordsForComparison::contains).count()
                            : 0L;

            // 인원
            int participantsMatch =
                    participant != null && participant > nowFlow.getParticipants()
                            ? participant - nowFlow.getParticipants()
                            : 0;

            // 연령대 겹치는 개수 확인
            List<Age> ageForComparison =
                    queryFactory
                            .select(flowAge.age)
                            .from(flowAge)
                            .where(flowAge.flow.id.eq(flowId))
                            .fetch();

            long ageMatchList =
                    age != null ? ageForComparison.stream().filter(age::contains).count() : 0L;

            // 겹치는 성별 확인
            List<Gender> genderForComparison =
                    queryFactory
                            .select(flowGender.gender)
                            .from(flowGender)
                            .where(flowGender.flow.id.eq(flowId))
                            .fetch();

            long genderMatchList =
                    gender != null
                            ? genderForComparison.stream().filter(gender::contains).count()
                            : 0L;
            flowList.add(
                    Triple.of(
                            nowFlow,
                            purposeMatchSize * 1.5 + loePlayTime * 0.005,
                            keywordMatchSize * 0.3
                                    + ageMatchList * 0.2
                                    + participantsMatch * 0.10
                                    + genderMatchList * 0.005));
        }

        // 가중치 정렬
        flowList.sort(Comparator.comparing(Triple<Flow, Double, Double>::getRight).reversed());

        flowList.sort(Comparator.comparing(Triple<Flow, Double, Double>::getMiddle).reversed());

        // 최대 가중치를 가지는 2개의 플로우 리턴
        return flowList.stream().map(Triple::getLeft).limit(2).collect(Collectors.toList());
    }

    @Override
    public Page<Flow> findScrapFlowsByUser(User user, Pageable pageable) {
        List<Flow> scrapFlows =
                queryFactory
                        .selectFrom(flow)
                        .where(
                                flow.flowScrapList.any().user.eq(user),
                                MaskingPredicates.mask(flow, user))
                        .fetch();

        JPQLQuery<Flow> countQuery =
                queryFactory
                        .selectFrom(flow)
                        .where(
                                flow.flowScrapList.any().user.eq(user),
                                MaskingPredicates.mask(flow, user));

        return PageableExecutionUtils.getPage(scrapFlows, pageable, countQuery::fetchCount);
    }
}
