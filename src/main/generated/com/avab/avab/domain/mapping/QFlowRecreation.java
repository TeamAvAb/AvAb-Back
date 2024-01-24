package com.avab.avab.domain.mapping;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFlowRecreation is a Querydsl query type for FlowRecreation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFlowRecreation extends EntityPathBase<FlowRecreation> {

    private static final long serialVersionUID = -496471779L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFlowRecreation flowRecreation = new QFlowRecreation("flowRecreation");

    public final NumberPath<Integer> customPlayTime = createNumber("customPlayTime", Integer.class);

    public final com.avab.avab.domain.QFlow flow;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.avab.avab.domain.QRecreation recreation;

    public QFlowRecreation(String variable) {
        this(FlowRecreation.class, forVariable(variable), INITS);
    }

    public QFlowRecreation(Path<? extends FlowRecreation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFlowRecreation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFlowRecreation(PathMetadata metadata, PathInits inits) {
        this(FlowRecreation.class, metadata, inits);
    }

    public QFlowRecreation(Class<? extends FlowRecreation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.flow = inits.isInitialized("flow") ? new com.avab.avab.domain.QFlow(forProperty("flow")) : null;
        this.recreation = inits.isInitialized("recreation") ? new com.avab.avab.domain.QRecreation(forProperty("recreation"), inits.get("recreation")) : null;
    }

}

