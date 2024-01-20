package com.avab.avab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecreationReview is a Querydsl query type for RecreationReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecreationReview extends EntityPathBase<RecreationReview> {

    private static final long serialVersionUID = 1749726183L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecreationReview recreationReview = new QRecreationReview("recreationReview");

    public final com.avab.avab.domain.common.QBaseEntity _super = new com.avab.avab.domain.common.QBaseEntity(this);

    public final QUser author;

    public final NumberPath<Integer> badCount = createNumber("badCount", Integer.class);

    public final StringPath contents = createString("contents");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> goodCount = createNumber("goodCount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QRecreation recreation;

    public final ListPath<com.avab.avab.domain.mapping.RecreationReviewRecommendation, com.avab.avab.domain.mapping.QRecreationReviewRecommendation> recreationReviewRecommendationList = this.<com.avab.avab.domain.mapping.RecreationReviewRecommendation, com.avab.avab.domain.mapping.QRecreationReviewRecommendation>createList("recreationReviewRecommendationList", com.avab.avab.domain.mapping.RecreationReviewRecommendation.class, com.avab.avab.domain.mapping.QRecreationReviewRecommendation.class, PathInits.DIRECT2);

    public final NumberPath<Integer> stars = createNumber("stars", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRecreationReview(String variable) {
        this(RecreationReview.class, forVariable(variable), INITS);
    }

    public QRecreationReview(Path<? extends RecreationReview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecreationReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecreationReview(PathMetadata metadata, PathInits inits) {
        this(RecreationReview.class, metadata, inits);
    }

    public QRecreationReview(Class<? extends RecreationReview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new QUser(forProperty("author")) : null;
        this.recreation = inits.isInitialized("recreation") ? new QRecreation(forProperty("recreation"), inits.get("recreation")) : null;
    }

}

