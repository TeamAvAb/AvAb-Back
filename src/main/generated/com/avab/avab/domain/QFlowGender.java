package com.avab.avab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFlowGender is a Querydsl query type for FlowGender
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFlowGender extends EntityPathBase<FlowGender> {

    private static final long serialVersionUID = -790971604L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFlowGender flowGender = new QFlowGender("flowGender");

    public final QFlow flow;

    public final EnumPath<com.avab.avab.domain.enums.Gender> gender = createEnum("gender", com.avab.avab.domain.enums.Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QFlowGender(String variable) {
        this(FlowGender.class, forVariable(variable), INITS);
    }

    public QFlowGender(Path<? extends FlowGender> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFlowGender(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFlowGender(PathMetadata metadata, PathInits inits) {
        this(FlowGender.class, metadata, inits);
    }

    public QFlowGender(Class<? extends FlowGender> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.flow = inits.isInitialized("flow") ? new QFlow(forProperty("flow")) : null;
    }

}

