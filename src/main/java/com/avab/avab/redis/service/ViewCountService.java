package com.avab.avab.redis.service;

import java.util.Map;

public interface ViewCountService {
    void incrementViewCount(Long id);

    Map<Long, Long> getTargetIdsAndViewCounts();
}
