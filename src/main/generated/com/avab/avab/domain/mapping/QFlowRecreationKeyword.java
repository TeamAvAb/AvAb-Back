package com.avab.avab.domain.mapping;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFlowRecreationKeyword is a Querydsl query type for FlowRecreationKeyword
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFlowRecreationKeyword extends EntityPathBase<FlowRecreationKeyword> {

    private static final long serialVersionUID = 1956002316L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFlowRecreationKeyword flowRecreationKeyword = new QFlowRecreationKeyword("flowRecreationKeyword");

    public final com.avab.avab.domain.QFlow flow;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.avab.avab.domain.QRecreationKeyword keyword;

    public QFlowRecreationKeyword(String variable) {
        this(FlowRecreationKeyword.class, forVariable(variable), INITS);
    }

    public QFlowRecreationKeyword(Path<? extends FlowRecreationKeyword> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFlowRecreationKeyword(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFlowRecreationKeyword(PathMetadata metadata, PathInits inits) {
        this(FlowRecreationKeyword.class, metadata, inits);
    }

    public QFlowRecreationKeyword(Class<? extends FlowRecreationKeyword> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.flow = inits.isInitialized("flow") ? new com.avab.avab.domain.QFlow(forProperty("flow")) : null;
        this.keyword = inits.isInitialized("keyword") ? new com.avab.avab.domain.QRecreationKeyword(forProperty("keyword")) : null;
    }

}

