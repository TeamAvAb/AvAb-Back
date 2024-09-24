package com.avab.avab.redis.repository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecreationViewCountLast7DaysRepositoryImpl extends BaseRedisRepository<Long>
        implements RecreationViewCountLast7DaysRepository {

    private final StringRedisTemplate redisTemplate;

    private final String VIEW_COUNT_LAST_7_DAYS_PREFIX = "recreationViewCountLast7Days";

    @Override
    public void incrementViewCountById(Long flowId) {
        redisTemplate.opsForValue().increment(createKey(flowId));
    }

    @Override
    public void createViewCountById(Long flowId) {
        redisTemplate
                .opsForValue()
                .setIfAbsent(createKey(flowId), "0", Duration.ofDays(7).plusHours(1));
    }

    @Override
    public List<Long> getAllTargetIds() {
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

        return keys.stream().map(this::extractId).toList();
    }

    @Override
    public List<Long> getViewCountsByIds(List<Long> recreationIds) {
        List<String> redisKeys = recreationIds.stream().map(this::createKey).toList();
        return redisTemplate.opsForValue().multiGet(redisKeys).stream()
                .map(viewCount -> viewCount != null ? Long.parseLong(viewCount) : 0L)
                .toList();
    }

    @Override
    protected String createKey(Long flowId) {
        return VIEW_COUNT_LAST_7_DAYS_PREFIX + ":" + flowId + ":" + LocalDate.now();
    }

    @Override
    protected Long extractId(String redisKey) {
        return Long.parseLong(redisKey.split(":")[1]);
    }
}
