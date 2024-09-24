package com.avab.avab.redis.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.joda.time.LocalDate;
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

    private final String VIEW_COUNT_PREFIX = "recreationViewCount";

    private final String VIEW_COUNT_LAST_7_DAYS_PREFIX = "recreationViewCountLast7Days";

    private final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public void incrementViewCount(String key) {
        redisTemplate.opsForValue().increment(VIEW_COUNT_PREFIX + ":" + key);
    }

    @Override
    public void createViewCount(String key) {
        redisTemplate.opsForValue().set(VIEW_COUNT_PREFIX + ":" + key, "0", 30, TimeUnit.MINUTES);
    }

    @Override
    public String getViewCount(String key) {
        return redisTemplate.opsForValue().get(VIEW_COUNT_PREFIX + ":" + key);
    }

    @Override
    public List<String> getAllRecreationIds() {
        ScanOptions scanOptions =
                ScanOptions.scanOptions().match(VIEW_COUNT_PREFIX + ":" + "*").count(100).build();

        Set<String> keys = new HashSet<>();
        try (Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
            while (cursor.hasNext()) {
                keys.add(cursor.next());
            }
        }

        return keys.stream().map(key -> key.split(":")[1]).toList();
    }

    @Override
    public void incrementViewCountLast7Days(String key) {
        String redisKey = createViewCountLast7DaysRedisKey(key, LocalDate.now());
        redisTemplate.opsForValue().increment(redisKey);
    }

    @Override
    public void createViewCountLast7Days(String key) {
        String redisKey = createViewCountLast7DaysRedisKey(key, LocalDate.now());
        redisTemplate.opsForValue().setIfAbsent(redisKey, "0");
        redisTemplate.expireAt(redisKey, LocalDate.now().plusDays(7).toDate());
    }

    @Override
    public Optional<String> getViewCountLast7Days(String key, LocalDate date) {
        String redisKey = createViewCountLast7DaysRedisKey(key, date);
        return Optional.ofNullable(redisTemplate.opsForValue().get(redisKey));
    }

    @Override
    public List<String> getAllFlowIdsToUpdateViewCountLast7Days() {
        ScanOptions scanOptions =
                ScanOptions.scanOptions()
                        .match(VIEW_COUNT_LAST_7_DAYS_PREFIX + ":" + "*")
                        .count(100)
                        .build();
        Set<String> keys = new HashSet<>();

        try (Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
            while (cursor.hasNext()) {
                keys.add(cursor.next());
            }
        }

        return keys.stream().map(key -> key.split(":")[1]).toList();
    }

    private String createViewCountLast7DaysRedisKey(String key, LocalDate date) {
        return VIEW_COUNT_LAST_7_DAYS_PREFIX + ":" + key + ":" + date.toString(DATE_FORMAT);
    }

    private String createViewCountRedisKey(String key) {
        return VIEW_COUNT_PREFIX + ":" + key;
    }

    @Override
    public List<String> getViewCountsByIds(List<String> recreationIds) {
        List<String> redisKeys = recreationIds.stream().map(this::createViewCountRedisKey).toList();
        return redisTemplate.opsForValue().multiGet(redisKeys);
    }
}
