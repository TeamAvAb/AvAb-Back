package com.avab.avab.feign.kakao;

import org.springframework.context.annotation.Bean;

import feign.Logger;
import feign.RequestInterceptor;

public class KaKaoOAuthFeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template ->
                template.header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
