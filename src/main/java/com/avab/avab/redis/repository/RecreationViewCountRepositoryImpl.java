package com.avab.avab.redis.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RecreationViewCountRepositoryImpl implements RecreationViewCountRepository {

    private final StringRedisTemplate redisTemplate;

    private final String PREFIX = "recreationViewCount";

    @Override
    public void incrementViewCount(String key) {
        redisTemplate.opsForValue().increment(PREFIX + ":" + key);
    }

    @Override
    public void createViewCount(String key) {
        redisTemplate.opsForValue().set(PREFIX + ":" + key, "0", 30, TimeUnit.MINUTES);
    }

    @Override
    public String getViewCount(String key) {
        return redisTemplate.opsForValue().get(PREFIX + ":" + key);
    }

    @Override
    public List<String> getAllRecreationIds() {
        ScanOptions scanOptions =
                ScanOptions.scanOptions().match(PREFIX + ":" + "*").count(100).build();
        Cursor<String> cursor = redisTemplate.scan(scanOptions);

        List<String> keys = new ArrayList<>();
        while (cursor.hasNext()) {
            keys.add(cursor.next());
        }

        return keys.stream().map(key -> key.split(":")[1]).toList();
    }
}
