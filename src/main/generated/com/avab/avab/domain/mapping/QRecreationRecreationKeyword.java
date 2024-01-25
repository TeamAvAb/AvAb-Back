package com.avab.avab.domain.mapping;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecreationRecreationKeyword is a Querydsl query type for RecreationRecreationKeyword
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecreationRecreationKeyword extends EntityPathBase<RecreationRecreationKeyword> {

    private static final long serialVersionUID = 1167761256L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecreationRecreationKeyword recreationRecreationKeyword = new QRecreationRecreationKeyword("recreationRecreationKeyword");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.avab.avab.domain.QRecreationKeyword keyword;

    public final com.avab.avab.domain.QRecreation recreation;

    public QRecreationRecreationKeyword(String variable) {
        this(RecreationRecreationKeyword.class, forVariable(variable), INITS);
    }

    public QRecreationRecreationKeyword(Path<? extends RecreationRecreationKeyword> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecreationRecreationKeyword(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecreationRecreationKeyword(PathMetadata metadata, PathInits inits) {
        this(RecreationRecreationKeyword.class, metadata, inits);
    }

    public QRecreationRecreationKeyword(Class<? extends RecreationRecreationKeyword> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.keyword = inits.isInitialized("keyword") ? new com.avab.avab.domain.QRecreationKeyword(forProperty("keyword")) : null;
        this.recreation = inits.isInitialized("recreation") ? new com.avab.avab.domain.QRecreation(forProperty("recreation"), inits.get("recreation")) : null;
    }

}

