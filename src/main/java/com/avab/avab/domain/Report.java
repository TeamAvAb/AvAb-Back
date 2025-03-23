package com.avab.avab.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.hibernate.annotations.ColumnDefault;

import com.avab.avab.domain.common.BaseEntity;
import com.avab.avab.domain.enums.ReportReason;
import com.avab.avab.domain.enums.ReportType;

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
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    private ReportType reportType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    @Builder.Default
    private User targetUser = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_recreation_id")
    @Builder.Default
    private Recreation targetRecreation = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_recreation_review_id")
    @Builder.Default
    private RecreationReview targetRecreationReview = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_flow_id")
    @Builder.Default
    private Flow targetFlow = null;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    private ReportReason reason;

    @Column(length = 300)
    @ColumnDefault("null")
    @Builder.Default
    private String extraReason = null;

    public Boolean isReporter(User user) {
        return this.reporter.equals(user);
    }
}
