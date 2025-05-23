package com.avab.avab.domain;

import java.time.LocalDateTime;
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

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.avab.avab.domain.common.BaseEntity;
import com.avab.avab.domain.mapping.FlowRecreation;
import com.avab.avab.domain.mapping.FlowRecreationKeyword;
import com.avab.avab.domain.mapping.FlowRecreationPurpose;
import com.avab.avab.domain.mapping.FlowScrap;

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

    @ColumnDefault("0")
    @Builder.Default
    private Long viewCount = 0L;

    @ColumnDefault("0")
    @Column(name = "view_count_last_7_days")
    @Builder.Default
    private Long viewCountLast7Days = 0L;

    @Column(length = 100)
    private String title;

    @ColumnDefault("0")
    @Builder.Default
    private Long scrapCount = 0L;

    @Column(length = 300)
    private String imageUrl;

    @Column
    @ColumnDefault("null")
    @Builder.Default
    private LocalDateTime deletedAt = null;

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
    private List<FlowScrap> flowScrapList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "targetFlow", cascade = CascadeType.ALL)
    private List<Report> reportList = new ArrayList<>();

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public Integer getReportCount() {
        return reportList.size();
    }

    public Boolean isAuthoredBy(User user) {
        return author.getId().equals(user.getId());
    }
}
