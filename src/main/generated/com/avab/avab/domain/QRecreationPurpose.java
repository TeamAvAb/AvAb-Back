package com.avab.avab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRecreationPurpose is a Querydsl query type for RecreationPurpose
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecreationPurpose extends EntityPathBase<RecreationPurpose> {

    private static final long serialVersionUID = 1381487215L;

    public static final QRecreationPurpose recreationPurpose = new QRecreationPurpose("recreationPurpose");

    public final com.avab.avab.domain.common.QBaseEntity _super = new com.avab.avab.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.avab.avab.domain.enums.Purpose> purpose = createEnum("purpose", com.avab.avab.domain.enums.Purpose.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRecreationPurpose(String variable) {
        super(RecreationPurpose.class, forVariable(variable));
    }

    public QRecreationPurpose(Path<? extends RecreationPurpose> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRecreationPurpose(PathMetadata metadata) {
        super(RecreationPurpose.class, metadata);
    }

}

