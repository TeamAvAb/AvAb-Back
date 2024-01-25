package com.avab.avab.domain.mapping;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecreationFavorite is a Querydsl query type for RecreationFavorite
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecreationFavorite extends EntityPathBase<RecreationFavorite> {

    private static final long serialVersionUID = 1938016907L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecreationFavorite recreationFavorite = new QRecreationFavorite("recreationFavorite");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.avab.avab.domain.QRecreation recreation;

    public final com.avab.avab.domain.QUser user;

    public QRecreationFavorite(String variable) {
        this(RecreationFavorite.class, forVariable(variable), INITS);
    }

    public QRecreationFavorite(Path<? extends RecreationFavorite> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecreationFavorite(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecreationFavorite(PathMetadata metadata, PathInits inits) {
        this(RecreationFavorite.class, metadata, inits);
    }

    public QRecreationFavorite(Class<? extends RecreationFavorite> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recreation = inits.isInitialized("recreation") ? new com.avab.avab.domain.QRecreation(forProperty("recreation"), inits.get("recreation")) : null;
        this.user = inits.isInitialized("user") ? new com.avab.avab.domain.QUser(forProperty("user")) : null;
    }

}

