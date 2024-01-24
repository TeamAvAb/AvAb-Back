package com.avab.avab.domain.mapping;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecreationRecreationPurpose is a Querydsl query type for RecreationRecreationPurpose
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecreationRecreationPurpose extends EntityPathBase<RecreationRecreationPurpose> {

    private static final long serialVersionUID = 1761705629L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecreationRecreationPurpose recreationRecreationPurpose = new QRecreationRecreationPurpose("recreationRecreationPurpose");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.avab.avab.domain.QRecreationPurpose purpose;

    public final com.avab.avab.domain.QRecreation recreation;

    public QRecreationRecreationPurpose(String variable) {
        this(RecreationRecreationPurpose.class, forVariable(variable), INITS);
    }

    public QRecreationRecreationPurpose(Path<? extends RecreationRecreationPurpose> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecreationRecreationPurpose(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecreationRecreationPurpose(PathMetadata metadata, PathInits inits) {
        this(RecreationRecreationPurpose.class, metadata, inits);
    }

    public QRecreationRecreationPurpose(Class<? extends RecreationRecreationPurpose> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.purpose = inits.isInitialized("purpose") ? new com.avab.avab.domain.QRecreationPurpose(forProperty("purpose")) : null;
        this.recreation = inits.isInitialized("recreation") ? new com.avab.avab.domain.QRecreation(forProperty("recreation"), inits.get("recreation")) : null;
    }

}

