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
}
