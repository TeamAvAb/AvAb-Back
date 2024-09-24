package com.avab.avab.redis.repository;

import java.util.List;
import java.util.Optional;

import org.joda.time.LocalDate;

public interface RecreationViewCountRepository {

    void incrementViewCountById(Long recreationId);

    void createViewCountById(Long recreationId);

    List<Long> getAllRecreationKeys();

    List<Long> getViewCountsByIds(List<Long> recreationIds);

    void incrementViewCountLast7Days(String key);

    void createViewCountLast7Days(String key);

    Optional<String> getViewCountLast7Days(String key, LocalDate date);

    List<String> getAllFlowIdsToUpdateViewCountLast7Days();
}
