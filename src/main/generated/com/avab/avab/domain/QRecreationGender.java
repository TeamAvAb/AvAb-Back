package com.avab.avab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecreationGender is a Querydsl query type for RecreationGender
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecreationGender extends EntityPathBase<RecreationGender> {

    private static final long serialVersionUID = 1434562384L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecreationGender recreationGender = new QRecreationGender("recreationGender");

    public final com.avab.avab.domain.common.QBaseEntity _super = new com.avab.avab.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.avab.avab.domain.enums.Gender> gender = createEnum("gender", com.avab.avab.domain.enums.Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QRecreation recreation;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRecreationGender(String variable) {
        this(RecreationGender.class, forVariable(variable), INITS);
    }

    public QRecreationGender(Path<? extends RecreationGender> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecreationGender(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecreationGender(PathMetadata metadata, PathInits inits) {
        this(RecreationGender.class, metadata, inits);
    }

    public QRecreationGender(Class<? extends RecreationGender> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recreation = inits.isInitialized("recreation") ? new QRecreation(forProperty("recreation"), inits.get("recreation")) : null;
    }

}

