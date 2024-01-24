package com.avab.avab.domain.mapping;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFlowRecreationPurpose is a Querydsl query type for FlowRecreationPurpose
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFlowRecreationPurpose extends EntityPathBase<FlowRecreationPurpose> {

    private static final long serialVersionUID = -1745020607L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFlowRecreationPurpose flowRecreationPurpose = new QFlowRecreationPurpose("flowRecreationPurpose");

    public final com.avab.avab.domain.QFlow flow;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.avab.avab.domain.QRecreationPurpose purpose;

    public QFlowRecreationPurpose(String variable) {
        this(FlowRecreationPurpose.class, forVariable(variable), INITS);
    }

    public QFlowRecreationPurpose(Path<? extends FlowRecreationPurpose> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFlowRecreationPurpose(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFlowRecreationPurpose(PathMetadata metadata, PathInits inits) {
        this(FlowRecreationPurpose.class, metadata, inits);
    }

    public QFlowRecreationPurpose(Class<? extends FlowRecreationPurpose> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.flow = inits.isInitialized("flow") ? new com.avab.avab.domain.QFlow(forProperty("flow")) : null;
        this.purpose = inits.isInitialized("purpose") ? new com.avab.avab.domain.QRecreationPurpose(forProperty("purpose")) : null;
    }

}

