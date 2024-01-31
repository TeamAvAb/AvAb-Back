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
import com.avab.avab.domain.enums.Purpose;
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
}
