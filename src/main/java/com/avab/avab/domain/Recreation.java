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
import com.avab.avab.domain.mapping.RecreationFavorite;
import com.avab.avab.domain.mapping.RecreationRecreationKeyword;
import com.avab.avab.domain.mapping.RecreationRecreationPurpose;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recreation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String title;

    @Column(length = 300)
    private String imageUrl;

    @ColumnDefault("0")
    @Builder.Default
    private Float totalStars = 0.0f;

    @Column(length = 300)
    private String summary;

    private Integer minParticipants;

    private Integer maxParticipants;

    private Integer playTime;

    @ColumnDefault("0")
    @Builder.Default
    private Long viewCount = 0L;

    @ColumnDefault("0")
    @Builder.Default
    private Long favoriteCount = 0L;

    @ColumnDefault("0")
    @Column(name = "view_count_last_7_days")
    @Builder.Default
    private Long viewCountLast7Days = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ColumnDefault("null")
    @Builder.Default
    private LocalDateTime deletedAt = null;

    @Builder.Default
    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationWay> recreationWayList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationAge> recreationAgeList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationGender> recreationGenderList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationHashtag> recreationHashTagsList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationRecreationKeyword> recreationRecreationKeywordList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationRecreationPurpose> recreationRecreationPurposeList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationPlace> recreationPlaceList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationPreparation> recreationPreparationList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationFavorite> recreationFavoriteList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationReview> recreationReviewList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<FlowRecreation> flowRecreationList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "targetRecreation", cascade = CascadeType.ALL)
    private List<Report> reportList = new ArrayList<>();

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public Boolean isDeleted() {
        return this.deletedAt != null;
    }

    public Boolean isAuthorDeleted() {
        return this.author.isDeleted();
    }

    public Boolean isReportedByUser(User user) {
        return this.reportList.stream().anyMatch(report -> report.isReporter(user));
    }

    public void incrementFavoriteCount() {
        this.favoriteCount++;
    }

    public void decrementFavoriteCount() {
        this.favoriteCount--;
    }
}
