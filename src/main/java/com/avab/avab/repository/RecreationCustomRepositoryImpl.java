package com.avab.avab.repository;

import static com.avab.avab.domain.QFlow.flow;
import static com.avab.avab.domain.QRecreation.recreation;
import static com.avab.avab.domain.QRecreationAge.recreationAge;
import static com.avab.avab.domain.QRecreationGender.recreationGender;
import static com.avab.avab.domain.QRecreationReview.recreationReview;
import static com.avab.avab.domain.mapping.QRecreationRecreationKeyword.recreationRecreationKeyword;
import static com.avab.avab.domain.mapping.QRecreationRecreationPurpose.recreationRecreationPurpose;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.RecreationException;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
import com.avab.avab.domain.enums.Purpose;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecreationCustomRepositoryImpl implements RecreationCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Recreation> searchRecreations(
            User user,
            String searchKeyword,
            List<Keyword> keywords,
            Integer participants,
            Integer playTime,
            List<Place> places,
            List<Purpose> purposes,
            List<Gender> genders,
            List<Age> ages,
            Pageable pageable) {
        JPQLQuery<Recreation> query =
                queryFactory
                        .select(recreation)
                        .from(recreation)
                        .where(
                                containsSearchKeyword(searchKeyword),
                                inKeyword(keywords),
                                betweenParticipants(participants),
                                loePlayTime(playTime),
                                inPlace(places),
                                inPurpose(purposes),
                                inGender(genders),
                                inAge(ages),
                                MaskingPredicates.mask(recreation, user))
                        .limit(pageable.getPageSize())
                        .offset(pageable.getOffset());

        QueryDslUtil.orderByFromSort(pageable.getSort(), recreation).forEach(query::orderBy);

        List<Recreation> recreationList = query.fetch();

        JPQLQuery<Long> countQuery =
                queryFactory
                        .select(recreation.count())
                        .from(recreation)
                        .where(
                                containsSearchKeyword(searchKeyword),
                                inKeyword(keywords),
                                betweenParticipants(participants),
                                loePlayTime(playTime),
                                inPlace(places),
                                inPurpose(purposes),
                                inGender(genders),
                                inAge(ages),
                                MaskingPredicates.mask(recreation, user));

        return PageableExecutionUtils.getPage(recreationList, pageable, countQuery::fetchOne);
    }

    private BooleanExpression containsSearchKeyword(String searchKeyword) {
        return searchKeyword != null
                ? recreation
                        .title
                        .contains(searchKeyword)
                        .or(recreation.summary.contains(searchKeyword))
                        .or(recreation.author.username.contains(searchKeyword))
                        .or(recreation.recreationHashTagsList.any().hashtag.contains(searchKeyword))
                : null;
    }

    private BooleanExpression inKeyword(List<Keyword> keywords) {
        return keywords != null
                ? recreation.recreationRecreationKeywordList.any().keyword.keyword.in(keywords)
                : null;
    }

    private BooleanExpression betweenParticipants(Integer participants) {
        return participants != null
                ? recreation
                        .minParticipants
                        .loe(participants)
                        .and(recreation.maxParticipants.goe(participants))
                : null;
    }

    private BooleanExpression loePlayTime(Integer playTime) {
        return playTime != null ? recreation.playTime.loe(playTime) : null;
    }

    private BooleanExpression inPlace(List<Place> places) {
        return places != null ? recreation.recreationPlaceList.any().place.in(places) : null;
    }

    private BooleanExpression inPurpose(List<Purpose> purposes) {
        return purposes != null
                ? recreation.recreationRecreationPurposeList.any().purpose.purpose.in(purposes)
                : null;
    }

    private BooleanExpression inGender(List<Gender> genders) {
        return genders != null ? recreation.recreationGenderList.any().gender.in(genders) : null;
    }

    private BooleanExpression inAge(List<Age> ages) {
        return ages != null ? recreation.recreationAgeList.any().age.in(ages) : null;
    }

    @Override
    public List<Recreation> findRelatedRecreations(
            Long recreationId,
            User user,
            List<Keyword> keyword,
            List<Purpose> purpose,
            Integer maxParticipants,
            List<Age> age) {
        List<Pair<Recreation, Double>> recreationList = new ArrayList<>();

        // 다른 Recreation id
        List<Long> otherRecreationIds =
                queryFactory
                        .select(recreation.id)
                        .from(recreation)
                        .where(
                                recreation.id.ne(recreationId),
                                MaskingPredicates.mask(recreation, user))
                        .fetch();

        // 다른 레크레이션들과 비교
        for (Long otherRecreationId : otherRecreationIds) {
            Recreation otherRecreation =
                    queryFactory
                            .selectFrom(recreation)
                            .where(recreation.id.eq(otherRecreationId))
                            .fetchOne();
            if (otherRecreation == null) {
                throw new RecreationException(ErrorStatus.RECREATION_NOT_FOUND);
            }

            // 겹치는 목적 체크
            List<Purpose> purposesForComparison =
                    queryFactory
                            .select(recreationRecreationPurpose.purpose.purpose)
                            .from(recreationRecreationPurpose)
                            .where(recreationRecreationPurpose.recreation.id.eq(otherRecreationId))
                            .fetch();

            long purposeMatchSize =
                    purposesForComparison.stream().filter(purpose::contains).count();

            // 겹치는 키워드 체크
            List<Keyword> keywordsForComparison =
                    queryFactory
                            .select(recreationRecreationKeyword.keyword.keyword)
                            .from(recreationRecreationKeyword)
                            .where(recreationRecreationKeyword.recreation.id.eq(otherRecreationId))
                            .fetch();

            long keywordMatchSize =
                    keyword.stream().filter(keywordsForComparison::contains).count();

            // 인원 기준 max인원과 다른 레크레이션의 min인원 차이확인
            int participantsMatch =
                    otherRecreation.getMinParticipants() != null
                                    && maxParticipants > otherRecreation.getMinParticipants()
                            ? maxParticipants - otherRecreation.getMinParticipants()
                            : 0;

            // 연령대 겹치는 개수 확인
            List<Age> ageForComparison =
                    queryFactory
                            .select(recreationAge.age)
                            .from(recreationAge)
                            .where(recreationAge.recreation.id.eq(otherRecreationId))
                            .fetch();

            long ageMatchList = ageForComparison.stream().filter(age::contains).count();

            // List에 추가
            recreationList.add(
                    Pair.of(
                            otherRecreation,
                            purposeMatchSize * 0.4
                                    + keywordMatchSize * 0.3
                                    + ageMatchList * 0.2
                                    + participantsMatch * 0.10));
        }

        // 가중치별 내림차순 정렬
        recreationList.sort(
                Comparator.comparingDouble(Pair<Recreation, Double>::getRight).reversed());

        // 최대 가중치를 가지는 2개의 레크레이션 리턴
        return recreationList.stream().map(Pair::getLeft).limit(2).collect(Collectors.toList());
    }

    @Override
    public List<Flow> findRelatedFlows(Long recreationId, User user) {

        return queryFactory
                .select(flow)
                .from(flow)
                .where(MaskingPredicates.mask(flow, user), isRecreationUsedInFlow(recreationId))
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(2)
                .fetch();
    }

    private BooleanExpression isRecreationUsedInFlow(Long recreationId) {
        return flow.flowRecreationList.any().recreation.id.eq(recreationId);
    }

    // 목적, 시간, 나머지는 연관 레크레이션과 같음 (키워드, 인원, 연령대, 성별)
    @Override
    public List<Recreation> recommendRecreations(
            List<Purpose> purpose,
            List<Keyword> keyword,
            List<Gender> gender,
            List<Age> age,
            Integer maxParticipant,
            Integer playTime,
            User user) {

        ArrayList<Triple<Recreation, Double, Double>> recreationList = new ArrayList<>();

        List<Long> recreations =
                queryFactory
                        .select(recreation.id)
                        .from(recreation)
                        .where(MaskingPredicates.mask(recreation, user))
                        .fetch();

        for (Long recreationId : recreations) {

            Recreation todayRecreation =
                    queryFactory
                            .selectFrom(recreation)
                            .where(recreation.id.eq(recreationId))
                            .fetchOne();

            // 겹치는 목적 체크
            List<Purpose> purposesForComparison =
                    queryFactory
                            .select(recreationRecreationPurpose.purpose.purpose)
                            .from(recreationRecreationPurpose)
                            .where(recreationRecreationPurpose.recreation.id.eq(recreationId))
                            .fetch();

            long purposeMatchSize =
                    purposesForComparison.stream().filter(purpose::contains).count();

            // 시간 내로 할수 있는 것인지
            long loePlayTime =
                    todayRecreation.getPlayTime() <= playTime
                            ? -Math.abs(todayRecreation.getPlayTime() - playTime)
                            : -10000;

            // 겹치는 키워드 체크
            List<Keyword> keywordsForComparison =
                    queryFactory
                            .select(recreationRecreationKeyword.keyword.keyword)
                            .from(recreationRecreationKeyword)
                            .where(recreationRecreationKeyword.recreation.id.eq(recreationId))
                            .fetch();

            long keywordMatchSize =
                    keyword != null
                            ? keyword.stream().filter(keywordsForComparison::contains).count()
                            : 0L;

            // 인원
            int participantsMatch =
                    maxParticipant != null && maxParticipant > todayRecreation.getMinParticipants()
                            ? maxParticipant - todayRecreation.getMinParticipants()
                            : 0;

            // 연령대 겹치는 개수 확인
            List<Age> ageForComparison =
                    queryFactory
                            .select(recreationAge.age)
                            .from(recreationAge)
                            .where(recreationAge.recreation.id.eq(recreationId))
                            .fetch();

            long ageMatchList =
                    age != null ? ageForComparison.stream().filter(age::contains).count() : 0L;

            // 겹치는 성별 확인
            List<Gender> genderForComparison =
                    queryFactory
                            .select(recreationGender.gender)
                            .from(recreationGender)
                            .where(recreationGender.recreation.id.eq(recreationId))
                            .fetch();

            long genderMatchList =
                    gender != null
                            ? genderForComparison.stream().filter(gender::contains).count()
                            : 0L;

            // List에 추가
            recreationList.add(
                    Triple.of(
                            todayRecreation,
                            purposeMatchSize * 1.5 + loePlayTime * 0.005,
                            keywordMatchSize * 0.3
                                    + ageMatchList * 0.2
                                    + participantsMatch * 0.10
                                    + genderMatchList * 0.005));
        }

        // 가중치별 내림차순 정렬 (중요하지 않은 것)
        recreationList.sort(
                Comparator.comparing(Triple<Recreation, Double, Double>::getRight).reversed());

        // Collection sort는 merge sort이기에 stable이 깨지지 않음.
        // 가중치별 내림차순 정렬 (중요한 것)
        recreationList.sort(
                Comparator.comparing(Triple<Recreation, Double, Double>::getMiddle).reversed());

        // 최대 가중치를 가지는 9개의 레크레이션 리턴
        return recreationList.stream().map(Triple::getLeft).limit(9).collect(Collectors.toList());
    }

    @Override
    public void updateTotalStars(Long recreationId) {
        Float totalStars =
                queryFactory
                        .select(
                                recreationReview
                                        .stars
                                        .sum()
                                        .divide(recreationReview.count())
                                        .floatValue())
                        .from(recreationReview)
                        .where(recreationReview.recreation.id.eq(recreationId))
                        .fetchOne();

        queryFactory
                .update(recreation)
                .set(recreation.totalStars, totalStars)
                .where(recreation.id.eq(recreationId))
                .execute();
    }

    @Override
    public Page<RecreationReview> findReviews(Long recreationId, User user, Pageable page) {
        List<RecreationReview> reviews =
                queryFactory
                        .selectFrom(recreationReview)
                        .where(
                                recreationReview.recreation.id.eq(recreationId),
                                MaskingPredicates.mask(recreationReview, user))
                        .orderBy(recreationReview.createdAt.desc())
                        .offset(page.getOffset())
                        .limit(page.getPageSize())
                        .fetch();

        JPQLQuery<Long> countQuery =
                queryFactory
                        .select(recreationReview.count())
                        .from(recreationReview)
                        .where(
                                recreationReview.recreation.id.eq(recreationId),
                                MaskingPredicates.mask(recreationReview, user));

        return PageableExecutionUtils.getPage(reviews, page, countQuery::fetchOne);
    }

    @Override
    public Page<Recreation> findFavoriteRecreationsByUser(User user, Pageable page) {
        List<Recreation> favoriteRecreations =
                queryFactory
                        .select(recreation)
                        .from(recreation)
                        .where(
                                recreation.recreationFavoriteList.any().user.eq(user),
                                MaskingPredicates.mask(recreation, user))
                        .offset(page.getOffset())
                        .limit(page.getPageSize())
                        .fetch();

        JPQLQuery<Long> countQuery =
                queryFactory
                        .select(recreation.count())
                        .from(recreation)
                        .where(
                                recreation.recreationFavoriteList.any().user.eq(user),
                                MaskingPredicates.mask(recreation, user));

        return PageableExecutionUtils.getPage(favoriteRecreations, page, countQuery::fetchOne);
    }
}
