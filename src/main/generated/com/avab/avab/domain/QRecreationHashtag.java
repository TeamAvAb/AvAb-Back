package com.avab.avab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecreationHashtag is a Querydsl query type for RecreationHashtag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecreationHashtag extends EntityPathBase<RecreationHashtag> {

    private static final long serialVersionUID = -1995468515L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecreationHashtag recreationHashtag = new QRecreationHashtag("recreationHashtag");

    public final StringPath hashtag = createString("hashtag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QRecreation recreation;

    public QRecreationHashtag(String variable) {
        this(RecreationHashtag.class, forVariable(variable), INITS);
    }

    public QRecreationHashtag(Path<? extends RecreationHashtag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecreationHashtag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecreationHashtag(PathMetadata metadata, PathInits inits) {
        this(RecreationHashtag.class, metadata, inits);
    }

    public QRecreationHashtag(Class<? extends RecreationHashtag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recreation = inits.isInitialized("recreation") ? new QRecreation(forProperty("recreation"), inits.get("recreation")) : null;
    }

}

