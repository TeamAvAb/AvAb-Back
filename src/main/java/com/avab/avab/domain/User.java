package com.avab.avab.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.avab.avab.constant.UserConstant;
import com.avab.avab.domain.common.BaseEntity;
import com.avab.avab.domain.enums.SocialType;
import com.avab.avab.domain.enums.UserStatus;
import com.avab.avab.domain.mapping.FlowScrap;
import com.avab.avab.domain.mapping.RecreationBookmark;
import com.avab.avab.domain.mapping.RecreationFavorite;
import com.avab.avab.domain.mapping.RecreationReviewRecommendation;

import lombok.*;

@Entity
@Builder
@Getter
@Setter
@DynamicInsert
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 200)
    private String email;

    @Column(length = 20)
    private String name;

    @Column(length = 50)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private SocialType socialType;

    @Column(length = 300)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private UserStatus userStatus;

    @ColumnDefault("null")
    private LocalDate deletedTime;

    @ColumnDefault("0")
    private Integer reportCount;

    private LocalDateTime disabledAt;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Recreation> recreationList = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Flow> flowList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecreationFavorite> recreationFavoriteList = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<RecreationReview> recreationReviewList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RecreationReviewRecommendation> recreationReviewRecommendationList =
            new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlowScrap> flowScrapList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RecreationBookmark> recreationBookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL)
    private List<Report> reportList = new ArrayList<>();

    public void deleteUser() {
        this.deletedTime = LocalDate.now();
        this.userStatus = UserStatus.DELETED;
    }

    public void incrementReportCount() {
        this.reportCount++;
    }

    public void disableUser() {
        this.disabledAt = LocalDateTime.now();
        this.userStatus = UserStatus.DISABLED;
    }

    public void enableUser() {
        this.disabledAt = null;
        this.userStatus = UserStatus.ENABLED;
    }

    public void restoreUser() {
        this.deletedTime = null;
        this.userStatus = UserStatus.ENABLED;
    }

    public Boolean isDisabled() {
        return this.userStatus == UserStatus.DISABLED;
    }

    public Boolean isCanBeEnabled() {
        return this.disabledAt != null
                && this.disabledAt
                        .plusDays(UserConstant.USER_DISABLE_PERIOD_DAYS)
                        .isBefore(LocalDateTime.now());
    }

    public Boolean isDeleted() {
        return this.userStatus == UserStatus.DELETED;
    }

    public void cancelFavoriteRecreation(Recreation recreation) {
        this.recreationFavoriteList.removeIf(it -> it.isTargetRecreation(recreation));
    }

    public void cancelScrapFlow(Flow flow) {
        this.flowScrapList.removeIf(it -> it.isTargetFlow(flow));
    }

    public void cancelRecommendationRecreationReview(RecreationReview recreationReview) {
        this.recreationReviewRecommendationList.removeIf(
                it -> it.isTargetRecreationReview(recreationReview));
    }

    public Boolean isFlowScrapped(Flow flow) {
        return this.flowScrapList.stream().anyMatch(it -> it.isTargetFlow(flow));
    }

    public Boolean isEnabled() {
        return this.userStatus == UserStatus.ENABLED;
    }
}
