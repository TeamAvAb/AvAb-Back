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
    private User targetUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_recreation_id")
    private Recreation targetRecreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_recreation_review_id")
    private RecreationReview targetRecreationReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_flow_id")
    private Flow targetFlow;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    private ReportReason reason;

    @Column(length = 300)
    private String extraReason;

    public Boolean isReporter(User user) {
        return this.reporter.equals(user);
    }
}
