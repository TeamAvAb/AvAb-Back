package com.avab.avab.slack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SlackChannel {
    SERVER_ERROR("C086U3W5KPU");

    private final String channelId;
}
