package com.avab.avab.redis.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class RecreationViewCountRepositoryImpl extends BaseRedisRepository<Long>
        implements RecreationViewCountRepository {

    private final StringRedisTemplate redisTemplate;

    private final String VIEW_COUNT_PREFIX = "recreationViewCount";

    @Override
    public void incrementViewCountById(Long recreationId) {
        String redisKey = createKey(recreationId);
        redisTemplate.opsForValue().increment(redisKey);
    }

    @Override
    public void createViewCountById(Long recreationId) {
        String redisKey = createKey(recreationId);
        redisTemplate.opsForValue().setIfAbsent(redisKey, "0", 30, TimeUnit.MINUTES);
    }

    @Override
    public List<Long> getAllTargetIds() {
        ScanOptions scanOptions =
                ScanOptions.scanOptions().match(VIEW_COUNT_PREFIX + ":" + "*").count(100).build();

        Set<String> keys = new HashSet<>();
        try (Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
            while (cursor.hasNext()) {
                keys.add(cursor.next());
            }
        }

        return keys.stream().map(this::extractId).toList();
    }

    @Override
    public List<Long> getViewCountsByIds(List<Long> ids) {
        List<String> redisKeys = ids.stream().map(this::createKey).toList();
        return redisTemplate.opsForValue().multiGet(redisKeys).stream()
                .map(viewCount -> viewCount != null ? Long.parseLong(viewCount) : 0L)
                .toList();
    }

    @Override
    protected String createKey(Long recreationId) {
        return VIEW_COUNT_PREFIX + ":" + recreationId.toString();
    }

    protected Long extractId(String redisKey) {
        return Long.parseLong(redisKey.split(":")[1]);
    }
}
