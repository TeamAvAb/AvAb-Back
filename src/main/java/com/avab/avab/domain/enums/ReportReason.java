package com.avab.avab.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReportReason {
    SPAM,
    DEFAMATION,
    ADULT_CONTENT,
    PRIVACY_EXPOSURE,
    VIOLENCE,
    COPYRIGHT_VIOLATION,
    OTHER;

    public static ReportReason fromString(String reason) {
        try {
            return ReportReason.valueOf(reason.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
