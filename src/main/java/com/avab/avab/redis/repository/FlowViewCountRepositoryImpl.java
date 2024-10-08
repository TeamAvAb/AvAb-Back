package com.avab.avab.redis.repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FlowViewCountRepositoryImpl extends BaseRedisRepository<Long>
        implements FlowViewCountRepository {

    private final StringRedisTemplate redisTemplate;

    private final String VIEW_COUNT_PREFIX = "flowViewCount";

    @Override
    public void incrementViewCountById(Long flowId) {
        redisTemplate.opsForValue().increment(createKey(flowId));
    }

    @Override
    public void createViewCountById(Long flowId) {
        redisTemplate.opsForValue().setIfAbsent(createKey(flowId), "0", Duration.ofMinutes(40));
    }

    @Override
    public List<Long> getAllTargetIds() {
        ScanOptions scanOptions =
                ScanOptions.scanOptions().match(VIEW_COUNT_PREFIX + ":" + "*").count(100).build();
        List<String> keys;

        try (Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
            keys = new ArrayList<>();
            while (cursor.hasNext()) {
                keys.add(cursor.next());
            }
        }

        return keys.stream().map(this::extractId).toList();
    }

    @Override
    public List<Long> getViewCountsByIds(List<Long> flowIds) {
        List<String> redisKeys = flowIds.stream().map(this::createKey).toList();
        return redisTemplate.opsForValue().multiGet(redisKeys).stream()
                .map(viewCount -> viewCount != null ? Long.parseLong(viewCount) : 0L)
                .toList();
    }

    @Override
    protected String createKey(Long flowId) {
        return VIEW_COUNT_PREFIX + ":" + flowId;
    }

    @Override
    protected Long extractId(String redisKey) {
        return Long.parseLong(redisKey.split(":")[1]);
    }
}
