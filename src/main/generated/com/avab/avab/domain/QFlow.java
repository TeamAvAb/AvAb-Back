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

    public static final QFlow flow = new QFlow("flow");

    public final com.avab.avab.domain.common.QBaseEntity _super = new com.avab.avab.domain.common.QBaseEntity(this);

    public final ListPath<FlowAge, QFlowAge> ageList = this.<FlowAge, QFlowAge>createList("ageList", FlowAge.class, QFlowAge.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

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

    public QFlow(String variable) {
        super(Flow.class, forVariable(variable));
    }

    public QFlow(Path<? extends Flow> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFlow(PathMetadata metadata) {
        super(Flow.class, metadata);
    }

}

