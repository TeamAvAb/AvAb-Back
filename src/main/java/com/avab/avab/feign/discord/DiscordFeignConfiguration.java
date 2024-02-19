package com.avab.avab.feign.discord;

import org.springframework.context.annotation.Bean;

import feign.Logger;
import feign.RequestInterceptor;

public class DiscordFeignConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> template.header("Content-Type", "application/json;charset=UTF-8");
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
