package com.avab.avab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecreationAge is a Querydsl query type for RecreationAge
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecreationAge extends EntityPathBase<RecreationAge> {

    private static final long serialVersionUID = -585720112L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecreationAge recreationAge = new QRecreationAge("recreationAge");

    public final EnumPath<com.avab.avab.domain.enums.Age> age = createEnum("age", com.avab.avab.domain.enums.Age.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QRecreation recreation;

    public QRecreationAge(String variable) {
        this(RecreationAge.class, forVariable(variable), INITS);
    }

    public QRecreationAge(Path<? extends RecreationAge> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecreationAge(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecreationAge(PathMetadata metadata, PathInits inits) {
        this(RecreationAge.class, metadata, inits);
    }

    public QRecreationAge(Class<? extends RecreationAge> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recreation = inits.isInitialized("recreation") ? new QRecreation(forProperty("recreation"), inits.get("recreation")) : null;
    }

}

