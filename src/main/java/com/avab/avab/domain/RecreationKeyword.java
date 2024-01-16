package com.avab.avab.domain;

import com.avab.avab.domain.common.BaseEntity;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.mapping.RecreationRecreationKeyword;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecreationKeyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(30)")
    private Keyword keyword;

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL)
    private List<RecreationRecreationKeyword> recreationRecreationKeywordList = new ArrayList<>();
}
