package com.avab.avab.domain.mapping;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecreationReviewRecommendation is a Querydsl query type for RecreationReviewRecommendation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecreationReviewRecommendation extends EntityPathBase<RecreationReviewRecommendation> {

    private static final long serialVersionUID = -471114848L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecreationReviewRecommendation recreationReviewRecommendation = new QRecreationReviewRecommendation("recreationReviewRecommendation");

    public final com.avab.avab.domain.common.QBaseEntity _super = new com.avab.avab.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.avab.avab.domain.QRecreationReview recreationReview;

    public final EnumPath<com.avab.avab.domain.enums.RecommendationType> type = createEnum("type", com.avab.avab.domain.enums.RecommendationType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.avab.avab.domain.QUser user;

    public QRecreationReviewRecommendation(String variable) {
        this(RecreationReviewRecommendation.class, forVariable(variable), INITS);
    }

    public QRecreationReviewRecommendation(Path<? extends RecreationReviewRecommendation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecreationReviewRecommendation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecreationReviewRecommendation(PathMetadata metadata, PathInits inits) {
        this(RecreationReviewRecommendation.class, metadata, inits);
    }

    public QRecreationReviewRecommendation(Class<? extends RecreationReviewRecommendation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recreationReview = inits.isInitialized("recreationReview") ? new com.avab.avab.domain.QRecreationReview(forProperty("recreationReview"), inits.get("recreationReview")) : null;
        this.user = inits.isInitialized("user") ? new com.avab.avab.domain.QUser(forProperty("user")) : null;
    }

}

