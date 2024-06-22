package com.avab.avab.redis.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.*;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@RedisHash(value = "refresh_token")
public class RefreshToken {
    @Id private Long id;
    private String token;
    @TimeToLive
    private Long expiration;
}
