package com.avab.avab.repository;

import static com.avab.avab.domain.QRecreation.recreation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.avab.avab.domain.QRecreation;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
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
            Keyword keyword,
            Integer participants,
            Integer playTime,
            Place place,
            Gender gender,
            Age age,
            Pageable pageable) {
        QRecreation recreation = QRecreation.recreation;

        List<Recreation> recreationList =
                queryFactory
                        .select(recreation)
                        .from(recreation)
                        .where(
                                containsSearchKeyword(searchKeyword),
                                eqKeyword(keyword),
                                betweenParticipants(participants),
                                loePlayTime(playTime),
                                eqPlace(place),
                                eqGender(gender),
                                eqAge(age))
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

    private BooleanExpression eqKeyword(Keyword keyword) {
        return keyword != null
                ? recreation.recreationRecreationKeywordList.any().keyword.keyword.eq(keyword)
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

    private BooleanExpression eqPlace(Place place) {
        return place != null ? recreation.recreationPlaceList.any().place.eq(place) : null;
    }

    private BooleanExpression eqGender(Gender gender) {
        return gender != null ? recreation.recreationGenderList.any().gender.eq(gender) : null;
    }

    private BooleanExpression eqAge(Age age) {
        return age != null ? recreation.recreationAgeList.any().age.eq(age) : null;
    }
}
