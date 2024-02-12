package com.avab.avab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCustomRecreation is a Querydsl query type for CustomRecreation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCustomRecreation extends EntityPathBase<CustomRecreation> {

    private static final long serialVersionUID = 1668002880L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCustomRecreation customRecreation = new QCustomRecreation("customRecreation");

    public final com.avab.avab.domain.mapping.QFlowRecreation flowRecreation;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> playTime = createNumber("playTime", Integer.class);

    public final ListPath<com.avab.avab.domain.mapping.RecreationRecreationKeyword, com.avab.avab.domain.mapping.QRecreationRecreationKeyword> recreationRecreationKeywordList = this.<com.avab.avab.domain.mapping.RecreationRecreationKeyword, com.avab.avab.domain.mapping.QRecreationRecreationKeyword>createList("recreationRecreationKeywordList", com.avab.avab.domain.mapping.RecreationRecreationKeyword.class, com.avab.avab.domain.mapping.QRecreationRecreationKeyword.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public QCustomRecreation(String variable) {
        this(CustomRecreation.class, forVariable(variable), INITS);
    }

    public QCustomRecreation(Path<? extends CustomRecreation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCustomRecreation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCustomRecreation(PathMetadata metadata, PathInits inits) {
        this(CustomRecreation.class, metadata, inits);
    }

    public QCustomRecreation(Class<? extends CustomRecreation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.flowRecreation = inits.isInitialized("flowRecreation") ? new com.avab.avab.domain.mapping.QFlowRecreation(forProperty("flowRecreation"), inits.get("flowRecreation")) : null;
    }

}

