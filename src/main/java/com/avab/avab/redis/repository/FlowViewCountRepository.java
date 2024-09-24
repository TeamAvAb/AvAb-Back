package com.avab.avab.redis.repository;

import java.util.List;
import java.util.Optional;

import org.joda.time.LocalDate;

public interface FlowViewCountRepository {

    void incrementViewCountById(Long flowId);

    void createViewCountById(Long flowId);

    List<Long> getAllFlowIds();

    List<Long> getViewCountsByIds(List<Long> keys);

    void incrementViewCountLast7Days(String key);

    void createViewCountLast7Days(String key);

    Optional<String> getViewCountLast7Days(String key, LocalDate date);

    List<String> getAllFlowIdsToUpdateViewCountLast7Days();
}
