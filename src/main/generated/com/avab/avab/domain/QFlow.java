package com.avab.avab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFlow is a Querydsl query type for Flow
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFlow extends EntityPathBase<Flow> {

    private static final long serialVersionUID = -1997426549L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFlow flow = new QFlow("flow");

    public final com.avab.avab.domain.common.QBaseEntity _super = new com.avab.avab.domain.common.QBaseEntity(this);

    public final ListPath<FlowAge, QFlowAge> ageList = this.<FlowAge, QFlowAge>createList("ageList", FlowAge.class, QFlowAge.class, PathInits.DIRECT2);

    public final QUser author;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<com.avab.avab.domain.mapping.FlowFavorite, com.avab.avab.domain.mapping.QFlowFavorite> flowFavoriteList = this.<com.avab.avab.domain.mapping.FlowFavorite, com.avab.avab.domain.mapping.QFlowFavorite>createList("flowFavoriteList", com.avab.avab.domain.mapping.FlowFavorite.class, com.avab.avab.domain.mapping.QFlowFavorite.class, PathInits.DIRECT2);

    public final ListPath<com.avab.avab.domain.mapping.FlowRecreationKeyword, com.avab.avab.domain.mapping.QFlowRecreationKeyword> flowRecreationKeywordList = this.<com.avab.avab.domain.mapping.FlowRecreationKeyword, com.avab.avab.domain.mapping.QFlowRecreationKeyword>createList("flowRecreationKeywordList", com.avab.avab.domain.mapping.FlowRecreationKeyword.class, com.avab.avab.domain.mapping.QFlowRecreationKeyword.class, PathInits.DIRECT2);

    public final ListPath<com.avab.avab.domain.mapping.FlowRecreation, com.avab.avab.domain.mapping.QFlowRecreation> flowRecreationList = this.<com.avab.avab.domain.mapping.FlowRecreation, com.avab.avab.domain.mapping.QFlowRecreation>createList("flowRecreationList", com.avab.avab.domain.mapping.FlowRecreation.class, com.avab.avab.domain.mapping.QFlowRecreation.class, PathInits.DIRECT2);

    public final ListPath<com.avab.avab.domain.mapping.FlowRecreationPurpose, com.avab.avab.domain.mapping.QFlowRecreationPurpose> flowRecreationPurposeList = this.<com.avab.avab.domain.mapping.FlowRecreationPurpose, com.avab.avab.domain.mapping.QFlowRecreationPurpose>createList("flowRecreationPurposeList", com.avab.avab.domain.mapping.FlowRecreationPurpose.class, com.avab.avab.domain.mapping.QFlowRecreationPurpose.class, PathInits.DIRECT2);

    public final ListPath<FlowGender, QFlowGender> genderList = this.<FlowGender, QFlowGender>createList("genderList", FlowGender.class, QFlowGender.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> participants = createNumber("participants", Integer.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> totalPlayTime = createNumber("totalPlayTime", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QFlow(String variable) {
        this(Flow.class, forVariable(variable), INITS);
    }

    public QFlow(Path<? extends Flow> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFlow(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFlow(PathMetadata metadata, PathInits inits) {
        this(Flow.class, metadata, inits);
    }

    public QFlow(Class<? extends Flow> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new QUser(forProperty("author")) : null;
    }

}

