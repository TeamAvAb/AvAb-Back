package com.avab.avab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecreation is a Querydsl query type for Recreation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecreation extends EntityPathBase<Recreation> {

    private static final long serialVersionUID = -2107928657L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecreation recreation = new QRecreation("recreation");

    public final com.avab.avab.domain.common.QBaseEntity _super = new com.avab.avab.domain.common.QBaseEntity(this);

    public final QUser author;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<com.avab.avab.domain.mapping.FlowRecreation, com.avab.avab.domain.mapping.QFlowRecreation> flowRecreationList = this.<com.avab.avab.domain.mapping.FlowRecreation, com.avab.avab.domain.mapping.QFlowRecreation>createList("flowRecreationList", com.avab.avab.domain.mapping.FlowRecreation.class, com.avab.avab.domain.mapping.QFlowRecreation.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final NumberPath<Integer> maxParticipants = createNumber("maxParticipants", Integer.class);

    public final NumberPath<Integer> minParticipants = createNumber("minParticipants", Integer.class);

    public final NumberPath<Integer> playTime = createNumber("playTime", Integer.class);

    public final ListPath<RecreationAge, QRecreationAge> recreationAgeList = this.<RecreationAge, QRecreationAge>createList("recreationAgeList", RecreationAge.class, QRecreationAge.class, PathInits.DIRECT2);

    public final ListPath<com.avab.avab.domain.mapping.RecreationFavorite, com.avab.avab.domain.mapping.QRecreationFavorite> recreationFavoriteList = this.<com.avab.avab.domain.mapping.RecreationFavorite, com.avab.avab.domain.mapping.QRecreationFavorite>createList("recreationFavoriteList", com.avab.avab.domain.mapping.RecreationFavorite.class, com.avab.avab.domain.mapping.QRecreationFavorite.class, PathInits.DIRECT2);

    public final ListPath<RecreationGender, QRecreationGender> recreationGenderList = this.<RecreationGender, QRecreationGender>createList("recreationGenderList", RecreationGender.class, QRecreationGender.class, PathInits.DIRECT2);

    public final ListPath<RecreationHashtag, QRecreationHashtag> recreationHashTagsList = this.<RecreationHashtag, QRecreationHashtag>createList("recreationHashTagsList", RecreationHashtag.class, QRecreationHashtag.class, PathInits.DIRECT2);

    public final ListPath<RecreationPlace, QRecreationPlace> recreationPlaceList = this.<RecreationPlace, QRecreationPlace>createList("recreationPlaceList", RecreationPlace.class, QRecreationPlace.class, PathInits.DIRECT2);

    public final ListPath<RecreationPreparation, QRecreationPreparation> recreationPreparationList = this.<RecreationPreparation, QRecreationPreparation>createList("recreationPreparationList", RecreationPreparation.class, QRecreationPreparation.class, PathInits.DIRECT2);

    public final ListPath<com.avab.avab.domain.mapping.RecreationRecreationKeyword, com.avab.avab.domain.mapping.QRecreationRecreationKeyword> recreationRecreationKeywordList = this.<com.avab.avab.domain.mapping.RecreationRecreationKeyword, com.avab.avab.domain.mapping.QRecreationRecreationKeyword>createList("recreationRecreationKeywordList", com.avab.avab.domain.mapping.RecreationRecreationKeyword.class, com.avab.avab.domain.mapping.QRecreationRecreationKeyword.class, PathInits.DIRECT2);

    public final ListPath<com.avab.avab.domain.mapping.RecreationRecreationPurpose, com.avab.avab.domain.mapping.QRecreationRecreationPurpose> recreationRecreationPurposeList = this.<com.avab.avab.domain.mapping.RecreationRecreationPurpose, com.avab.avab.domain.mapping.QRecreationRecreationPurpose>createList("recreationRecreationPurposeList", com.avab.avab.domain.mapping.RecreationRecreationPurpose.class, com.avab.avab.domain.mapping.QRecreationRecreationPurpose.class, PathInits.DIRECT2);

    public final ListPath<RecreationReview, QRecreationReview> recreationReviewList = this.<RecreationReview, QRecreationReview>createList("recreationReviewList", RecreationReview.class, QRecreationReview.class, PathInits.DIRECT2);

    public final ListPath<RecreationWay, QRecreationWay> recreationWayList = this.<RecreationWay, QRecreationWay>createList("recreationWayList", RecreationWay.class, QRecreationWay.class, PathInits.DIRECT2);

    public final StringPath summary = createString("summary");

    public final StringPath title = createString("title");

    public final NumberPath<Float> totalStars = createNumber("totalStars", Float.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QRecreation(String variable) {
        this(Recreation.class, forVariable(variable), INITS);
    }

    public QRecreation(Path<? extends Recreation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecreation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecreation(PathMetadata metadata, PathInits inits) {
        this(Recreation.class, metadata, inits);
    }

    public QRecreation(Class<? extends Recreation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new QUser(forProperty("author")) : null;
    }

}

