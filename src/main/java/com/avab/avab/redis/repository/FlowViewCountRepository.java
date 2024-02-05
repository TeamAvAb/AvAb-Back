package com.avab.avab.redis.repository;

import java.util.List;
import java.util.Optional;

public interface FlowViewCountRepository {

    void incrementViewCount(String key);

    void createViewCount(String key);

    Optional<String> getViewCount(String key);

    List<String> getAllFlowIds();
}
