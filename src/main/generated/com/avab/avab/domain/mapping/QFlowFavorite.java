package com.avab.avab.domain.mapping;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFlowFavorite is a Querydsl query type for FlowFavorite
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFlowFavorite extends EntityPathBase<FlowFavorite> {

    private static final long serialVersionUID = 19195751L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFlowFavorite flowFavorite = new QFlowFavorite("flowFavorite");

    public final com.avab.avab.domain.QFlow flow;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.avab.avab.domain.QUser user;

    public QFlowFavorite(String variable) {
        this(FlowFavorite.class, forVariable(variable), INITS);
    }

    public QFlowFavorite(Path<? extends FlowFavorite> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFlowFavorite(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFlowFavorite(PathMetadata metadata, PathInits inits) {
        this(FlowFavorite.class, metadata, inits);
    }

    public QFlowFavorite(Class<? extends FlowFavorite> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.flow = inits.isInitialized("flow") ? new com.avab.avab.domain.QFlow(forProperty("flow")) : null;
        this.user = inits.isInitialized("user") ? new com.avab.avab.domain.QUser(forProperty("user")) : null;
    }

}

