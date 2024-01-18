package com.avab.avab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecreationKeyword is a Querydsl query type for RecreationKeyword
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecreationKeyword extends EntityPathBase<RecreationKeyword> {

    private static final long serialVersionUID = 787542842L;

    public static final QRecreationKeyword recreationKeyword = new QRecreationKeyword("recreationKeyword");

    public final com.avab.avab.domain.common.QBaseEntity _super = new com.avab.avab.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final EnumPath<com.avab.avab.domain.enums.Keyword> keyword = createEnum("keyword", com.avab.avab.domain.enums.Keyword.class);

    public final ListPath<com.avab.avab.domain.mapping.RecreationRecreationKeyword, com.avab.avab.domain.mapping.QRecreationRecreationKeyword> recreationRecreationKeywordList = this.<com.avab.avab.domain.mapping.RecreationRecreationKeyword, com.avab.avab.domain.mapping.QRecreationRecreationKeyword>createList("recreationRecreationKeywordList", com.avab.avab.domain.mapping.RecreationRecreationKeyword.class, com.avab.avab.domain.mapping.QRecreationRecreationKeyword.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRecreationKeyword(String variable) {
        super(RecreationKeyword.class, forVariable(variable));
    }

    public QRecreationKeyword(Path<? extends RecreationKeyword> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRecreationKeyword(PathMetadata metadata) {
        super(RecreationKeyword.class, metadata);
    }

}

