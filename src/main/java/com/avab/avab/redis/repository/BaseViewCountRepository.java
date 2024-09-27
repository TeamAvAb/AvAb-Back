package com.avab.avab.redis.repository;

import java.util.List;

public interface BaseViewCountRepository {
    void incrementViewCountById(Long flowId);

    void createViewCountById(Long flowId);

    List<Long> getAllTargetIds();

    List<Long> getViewCountsByIds(List<Long> ids);
}
