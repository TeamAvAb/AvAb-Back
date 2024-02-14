package com.avab.avab.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.avab.avab.domain.common.BaseEntity;
import com.avab.avab.domain.mapping.FlowFavorite;
import com.avab.avab.domain.mapping.FlowRecreation;
import com.avab.avab.domain.mapping.FlowRecreationKeyword;
import com.avab.avab.domain.mapping.FlowRecreationPurpose;

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
@DynamicUpdate
@DynamicInsert
public class Flow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer totalPlayTime;

    private Integer participants;

    private Long viewCount;

    @Column(length = 100)
    private String title;

    private Long scrapCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Builder.Default
    @OneToMany(mappedBy = "flow", cascade = CascadeType.ALL)
    private List<FlowAge> ageList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "flow", cascade = CascadeType.ALL)
    private List<FlowGender> genderList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "flow", cascade = CascadeType.ALL)
    private List<FlowRecreation> flowRecreationList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "flow", cascade = CascadeType.ALL)
    private List<FlowRecreationKeyword> flowRecreationKeywordList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "flow", cascade = CascadeType.ALL)
    private List<FlowRecreationPurpose> flowRecreationPurposeList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "flow", cascade = CascadeType.ALL)
    private List<FlowFavorite> flowFavoriteList = new ArrayList<>();
}
