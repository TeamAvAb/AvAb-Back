package com.avab.avab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecreationPreparation is a Querydsl query type for RecreationPreparation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecreationPreparation extends EntityPathBase<RecreationPreparation> {

    private static final long serialVersionUID = -1718162584L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecreationPreparation recreationPreparation = new QRecreationPreparation("recreationPreparation");

    public final com.avab.avab.domain.common.QBaseEntity _super = new com.avab.avab.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QRecreation recreation;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRecreationPreparation(String variable) {
        this(RecreationPreparation.class, forVariable(variable), INITS);
    }

    public QRecreationPreparation(Path<? extends RecreationPreparation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecreationPreparation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecreationPreparation(PathMetadata metadata, PathInits inits) {
        this(RecreationPreparation.class, metadata, inits);
    }

    public QRecreationPreparation(Class<? extends RecreationPreparation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recreation = inits.isInitialized("recreation") ? new QRecreation(forProperty("recreation"), inits.get("recreation")) : null;
    }

}

