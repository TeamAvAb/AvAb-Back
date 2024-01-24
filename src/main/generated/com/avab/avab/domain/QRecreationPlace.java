package com.avab.avab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecreationPlace is a Querydsl query type for RecreationPlace
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecreationPlace extends EntityPathBase<RecreationPlace> {

    private static final long serialVersionUID = -222310760L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecreationPlace recreationPlace = new QRecreationPlace("recreationPlace");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.avab.avab.domain.enums.Place> place = createEnum("place", com.avab.avab.domain.enums.Place.class);

    public final QRecreation recreation;

    public QRecreationPlace(String variable) {
        this(RecreationPlace.class, forVariable(variable), INITS);
    }

    public QRecreationPlace(Path<? extends RecreationPlace> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecreationPlace(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecreationPlace(PathMetadata metadata, PathInits inits) {
        this(RecreationPlace.class, metadata, inits);
    }

    public QRecreationPlace(Class<? extends RecreationPlace> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recreation = inits.isInitialized("recreation") ? new QRecreation(forProperty("recreation"), inits.get("recreation")) : null;
    }

}

