package com.avab.avab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecreationWay is a Querydsl query type for RecreationWay
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecreationWay extends EntityPathBase<RecreationWay> {

    private static final long serialVersionUID = -585699136L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecreationWay recreationWay = new QRecreationWay("recreationWay");

    public final com.avab.avab.domain.common.QBaseEntity _super = new com.avab.avab.domain.common.QBaseEntity(this);

    public final StringPath contents = createString("contents");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final QRecreation recreation;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRecreationWay(String variable) {
        this(RecreationWay.class, forVariable(variable), INITS);
    }

    public QRecreationWay(Path<? extends RecreationWay> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecreationWay(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecreationWay(PathMetadata metadata, PathInits inits) {
        this(RecreationWay.class, metadata, inits);
    }

    public QRecreationWay(Class<? extends RecreationWay> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recreation = inits.isInitialized("recreation") ? new QRecreation(forProperty("recreation"), inits.get("recreation")) : null;
    }

}

