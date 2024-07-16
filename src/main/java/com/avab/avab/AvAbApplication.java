package com.avab.avab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableRedisRepositories
@EnableFeignClients
@EnableScheduling
public class AvAbApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvAbApplication.class, args);
    }
}
