package com.avab.avab.repository;

import static com.avab.avab.domain.QRecreation.recreation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.avab.avab.domain.QRecreationGender;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.RecreationException;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.QFlow;
import com.avab.avab.domain.QRecreation;
import com.avab.avab.domain.QRecreationAge;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.domain.mapping.QRecreationRecreationKeyword;
import com.avab.avab.domain.mapping.QRecreationRecreationPurpose;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecreationCustomRepositoryImpl implements RecreationCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Recreation> searchRecreations(
            String searchKeyword,
            List<Keyword> keywords,
            Integer participants,
            Integer playTime,
            List<Place> places,
            List<Purpose> purposes,
            List<Gender> genders,
            List<Age> ages,
            Pageable pageable) {
        QRecreation recreation = QRecreation.recreation;

        List<Recreation> recreationList =
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
                                inAge(ages))
                        .limit(pageable.getPageSize())
                        .offset(pageable.getOffset())
                        .fetch();

        return new PageImpl<>(recreationList);
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
                        .goe(participants)
                        .and(recreation.maxParticipants.loe(participants))
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
            List<Keyword> keyword,
            List<Purpose> purpose,
            Integer maxParticipants,
            List<Age> age) {
        QRecreation recreation = QRecreation.recreation;
        QRecreationRecreationPurpose recreationPurpose =
                QRecreationRecreationPurpose.recreationRecreationPurpose;
        QRecreationRecreationKeyword recreationKeyword =
                QRecreationRecreationKeyword.recreationRecreationKeyword;
        QRecreationAge recreationAge = QRecreationAge.recreationAge;
        List<Pair<Recreation, Double>> recreationList = new ArrayList<>();

        // 다른 Recreation id
        List<Long> otherRecreationIds =
                queryFactory
                        .select(recreation.id)
                        .from(recreation)
                        .where(recreation.id.ne(recreationId))
                        .fetch();

        // 다른 레크레이션들과 비교
        for (Long otherRecreationId : otherRecreationIds) {
            Recreation otherRecreation =
                    queryFactory
                            .selectFrom(recreation)
                            .where(recreation.id.eq(otherRecreationId))
                            .fetchOne();
            if (otherRecreation == null)
                throw new RecreationException(ErrorStatus.RECREATION_NOT_FOUND);

            // 겹치는 목적 체크
            List<Purpose> purposesForComparison =
                    queryFactory
                            .select(recreationPurpose.purpose.purpose)
                            .from(recreationPurpose)
                            .where(recreationPurpose.recreation.id.eq(otherRecreationId))
                            .fetch();

            long purposeMatchSize =
                    purposesForComparison.stream().filter(purpose::contains).count();

            // 겹치는 키워드 체크
            List<Keyword> keywordsForComparison =
                    queryFactory
                            .select(recreationKeyword.keyword.keyword)
                            .from(recreationKeyword)
                            .where(recreationKeyword.recreation.id.eq(otherRecreationId))
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
    public List<Flow> findRelatedFlows(Long recreationId) {
        QFlow flow = QFlow.flow;
        List<Flow> randFlows = new ArrayList<>();

        List<Flow> flows =
                queryFactory
                        .select(flow)
                        .from(flow)
                        .where(flow.flowRecreationList.any().recreation.id.eq(recreationId))
                        .fetch();

        // flow 사이즈가 2를 넘으면 랜덤으로 2개 리턴하기
        if (flows.size() > 2) {
            List<Flow> shuffledFlows = new ArrayList<>(flows);
            Collections.shuffle(shuffledFlows);

            randFlows.add(shuffledFlows.get(0));
            randFlows.add(shuffledFlows.get(1));
            return randFlows;
        } else return flows;
    }

    // 목적, 시간, 나머지는 연관 레크레이션과 같음 (키워드, 인원, 연령대, 성별)
    @Override
    public List<Recreation> recommendRecreations(List<Purpose> purpose, List<Keyword> keyword, List<Gender> gender, List<Age> age, Integer maxParticipant, Integer playTime) {
        QRecreation recreation = QRecreation.recreation;

        QRecreationRecreationPurpose recreationPurpose =
                QRecreationRecreationPurpose.recreationRecreationPurpose;
        QRecreationRecreationKeyword recreationKeyword =
                QRecreationRecreationKeyword.recreationRecreationKeyword;
        QRecreationAge recreationAge = QRecreationAge.recreationAge;
        QRecreationGender recreationGender = QRecreationGender.recreationGender;

        ArrayList<Triple<Recreation, Double, Double>> recreationList = new ArrayList<>();

        List<Long> recreations = queryFactory.select(recreation.id)
                                        .from(recreation)
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
                            .select(recreationPurpose.purpose.purpose)
                            .from(recreationPurpose)
                            .where(recreationPurpose.recreation.id.eq(recreationId))
                            .fetch();

            long purposeMatchSize =
                    purposesForComparison.stream().filter(purpose::contains).count();

            // 시간 내로 할수 있는 것인지
            long loePlayTime = todayRecreation.getPlayTime() <= playTime ? -Math.abs(todayRecreation.getPlayTime() - playTime) : -10000;

            // 겹치는 키워드 체크
            List<Keyword> keywordsForComparison =
                    queryFactory
                            .select(recreationKeyword.keyword.keyword)
                            .from(recreationKeyword)
                            .where(recreationKeyword.recreation.id.eq(recreationId))
                            .fetch();

            long keywordMatchSize =
                    keyword != null ? keyword.stream().filter(keywordsForComparison::contains).count() : 0l;

            // 인원
            int participantsMatch =
                    maxParticipant != null
                            && maxParticipant > todayRecreation.getMinParticipants()
                            ? maxParticipant - todayRecreation.getMinParticipants()
                            : 0;

            // 연령대 겹치는 개수 확인
            List<Age> ageForComparison =
                    queryFactory
                            .select(recreationAge.age)
                            .from(recreationAge)
                            .where(recreationAge.recreation.id.eq(recreationId))
                            .fetch();

            long ageMatchList = age != null ? ageForComparison.stream().filter(age::contains).count() : 0l;

            // 겹치는 성별 확인
            List<Gender> genderForComparison =
                    queryFactory
                            .select(recreationGender.gender)
                            .from(recreationGender)
                            .where(recreationGender.recreation.id.eq(recreationId))
                            .fetch();

            long genderMatchList = gender != null ? genderForComparison.stream().filter(gender::contains).count() : 0l;



            // List에 추가
            recreationList.add(
                    Triple.of(
                            todayRecreation,
                            purposeMatchSize * 1.5 + loePlayTime * 0.005,
                                    keywordMatchSize * 0.3
                                            + ageMatchList * 0.2
                                            + participantsMatch * 0.10
                                            + genderMatchList * 0.005)

            );
        }


        // 가중치별 내림차순 정렬 (중요하지 않은 것)
        recreationList.sort(
                Comparator.comparing(Triple<Recreation,Double, Double>::getRight).reversed());

        // Collection sort는 merge sort이기에 stable이 깨지지 않음.
        // 가중치별 내림차순 정렬 (중요한 것)
        recreationList.sort(
                Comparator.comparing(Triple<Recreation, Double, Double>::getMiddle).reversed());


        // 최대 가중치를 가지는 9개의 레크레이션 리턴
        return recreationList.stream().map(Triple::getLeft).limit(9).collect(Collectors.toList());
    }
}
