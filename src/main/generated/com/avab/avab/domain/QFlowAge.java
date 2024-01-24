package com.avab.avab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFlowAge is a Querydsl query type for FlowAge
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFlowAge extends EntityPathBase<FlowAge> {

    private static final long serialVersionUID = 1437630580L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFlowAge flowAge = new QFlowAge("flowAge");

    public final EnumPath<com.avab.avab.domain.enums.Age> age = createEnum("age", com.avab.avab.domain.enums.Age.class);

    public final QFlow flow;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QFlowAge(String variable) {
        this(FlowAge.class, forVariable(variable), INITS);
    }

    public QFlowAge(Path<? extends FlowAge> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFlowAge(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFlowAge(PathMetadata metadata, PathInits inits) {
        this(FlowAge.class, metadata, inits);
    }

    public QFlowAge(Class<? extends FlowAge> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.flow = inits.isInitialized("flow") ? new QFlow(forProperty("flow")) : null;
    }

}

