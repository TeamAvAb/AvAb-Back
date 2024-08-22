package com.avab.avab.redis.repository;

import java.util.List;
import java.util.Optional;

import org.joda.time.LocalDate;

public interface RecreationViewCountRepository {

    void incrementViewCount(String key);

    void createViewCount(String key);

    String getViewCount(String key);

    List<String> getAllRecreationIds();

    void incrementViewCountLast7Days(String key);

    void createViewCountLast7Days(String key);

    Optional<String> getViewCountLast7Days(String key, LocalDate date);

    List<String> getAllFlowIdsToUpdateViewCountLast7Days();

    String createViewCountLast7DaysRedisKey(String key, LocalDate date);
}
