package com.avab.avab.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLRestriction;

import com.avab.avab.domain.common.BaseEntity;
import com.avab.avab.domain.enums.SocialType;
import com.avab.avab.domain.enums.UserStatus;
import com.avab.avab.domain.mapping.FlowFavorite;
import com.avab.avab.domain.mapping.RecreationFavorite;
import com.avab.avab.domain.mapping.RecreationReviewRecommendation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@DynamicInsert
@SQLRestriction("user_status = 'ENABLED' OR user_status = 'DISABLED'")
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RecreationFavorite> recreationFavoriteList = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<RecreationReview> recreationReviewList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RecreationReviewRecommendation> recreationReviewRecommendationList =
            new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FlowFavorite> flowFavoriteList = new ArrayList<>();

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

    public Boolean isDisabled() {
        return this.userStatus == UserStatus.DISABLED;
    }
}
