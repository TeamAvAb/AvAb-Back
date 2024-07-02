package com.avab.avab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1996973272L;

    public static final QUser user = new QUser("user");

    public final com.avab.avab.domain.common.QBaseEntity _super = new com.avab.avab.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> deletedTime = createDate("deletedTime", java.time.LocalDate.class);

    public final StringPath email = createString("email");

    public final ListPath<com.avab.avab.domain.mapping.FlowFavorite, com.avab.avab.domain.mapping.QFlowFavorite> flowFavoriteList = this.<com.avab.avab.domain.mapping.FlowFavorite, com.avab.avab.domain.mapping.QFlowFavorite>createList("flowFavoriteList", com.avab.avab.domain.mapping.FlowFavorite.class, com.avab.avab.domain.mapping.QFlowFavorite.class, PathInits.DIRECT2);

    public final ListPath<Flow, QFlow> flowList = this.<Flow, QFlow>createList("flowList", Flow.class, QFlow.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath profileImage = createString("profileImage");

    public final ListPath<com.avab.avab.domain.mapping.RecreationFavorite, com.avab.avab.domain.mapping.QRecreationFavorite> recreationFavoriteList = this.<com.avab.avab.domain.mapping.RecreationFavorite, com.avab.avab.domain.mapping.QRecreationFavorite>createList("recreationFavoriteList", com.avab.avab.domain.mapping.RecreationFavorite.class, com.avab.avab.domain.mapping.QRecreationFavorite.class, PathInits.DIRECT2);

    public final ListPath<Recreation, QRecreation> recreationList = this.<Recreation, QRecreation>createList("recreationList", Recreation.class, QRecreation.class, PathInits.DIRECT2);

    public final ListPath<RecreationReview, QRecreationReview> recreationReviewList = this.<RecreationReview, QRecreationReview>createList("recreationReviewList", RecreationReview.class, QRecreationReview.class, PathInits.DIRECT2);

    public final ListPath<com.avab.avab.domain.mapping.RecreationReviewRecommendation, com.avab.avab.domain.mapping.QRecreationReviewRecommendation> recreationReviewRecommendationList = this.<com.avab.avab.domain.mapping.RecreationReviewRecommendation, com.avab.avab.domain.mapping.QRecreationReviewRecommendation>createList("recreationReviewRecommendationList", com.avab.avab.domain.mapping.RecreationReviewRecommendation.class, com.avab.avab.domain.mapping.QRecreationReviewRecommendation.class, PathInits.DIRECT2);

    public final EnumPath<com.avab.avab.domain.enums.SocialType> socialType = createEnum("socialType", com.avab.avab.domain.enums.SocialType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath username = createString("username");

    public final EnumPath<com.avab.avab.domain.enums.UserStatus> userStatus = createEnum("userStatus", com.avab.avab.domain.enums.UserStatus.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

