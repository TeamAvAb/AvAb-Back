package com.avab.avab.repository;

import static com.avab.avab.domain.QRecreation.recreation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.RecreationException;
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

    private BooleanExpression inGender(List<Gender> genders) {
        return genders != null ? recreation.recreationGenderList.any().gender.in(genders) : null;
    }

    private BooleanExpression inAge(List<Age> ages) {
        return ages != null ? recreation.recreationAgeList.any().age.in(ages) : null;
    }

    @Override
    public List<Recreation> relatedRecreations(
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
                                    + ageMatchList * 0.15
                                    + participantsMatch * 0.15));
        }

        // 가중치별 내림차순 정렬
        recreationList.sort(
                Comparator.comparingDouble(Pair<Recreation, Double>::getRight).reversed());

        // 최대 가중치를 가지는 2개의 레크레이션 리턴
        return recreationList.stream().map(Pair::getLeft).limit(2).collect(Collectors.toList());
    }
}
