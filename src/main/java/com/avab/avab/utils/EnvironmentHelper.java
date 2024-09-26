package com.avab.avab.utils;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EnvironmentHelper {

    private final Environment environment;

    public Boolean isLocal() {
        return environment.acceptsProfiles(Profiles.of("local"));
    }

    public Boolean isDev() {
        return environment.acceptsProfiles(Profiles.of("dev"));
    }

    public Boolean isProd() {
        return environment.acceptsProfiles(Profiles.of("prod"));
    }
}
