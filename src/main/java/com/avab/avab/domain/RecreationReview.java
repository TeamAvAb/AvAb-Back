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
import com.avab.avab.domain.mapping.RecreationReviewRecommendation;

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
public class RecreationReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer stars;

    @Column(length = 500)
    private String content;

    @ColumnDefault("0")
    @Builder.Default
    private Integer goodCount = 0;

    @ColumnDefault("0")
    @Builder.Default
    private Integer badCount = 0;

    @ColumnDefault("null")
    @Builder.Default
    private LocalDateTime deletedAt = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recreation_id")
    private Recreation recreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(mappedBy = "recreationReview", cascade = CascadeType.ALL)
    private List<RecreationReviewRecommendation> recreationReviewRecommendationList =
            new ArrayList<>();

    @OneToMany(mappedBy = "targetRecreationReview", cascade = CascadeType.ALL)
    private List<Report> reportList = new ArrayList<>();

    public Integer getReportCount() {
        return reportList.size();
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void incrementGoodCount() {
        this.goodCount++;
    }

    public void incrementBadCount() {
        this.badCount++;
    }

    public void decrementGoodCount() {
        this.goodCount--;
    }

    public void decrementBadCount() {
        this.badCount--;
    }
}
