package com.avab.avab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
public class AvAbApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvAbApplication.class, args);
    }
}
