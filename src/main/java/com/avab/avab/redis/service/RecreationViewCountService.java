package com.avab.avab.redis.service;

import java.util.List;
import java.util.Map;

public interface RecreationViewCountService {

    void incrementViewCount(Long recreationId);

    Map<Long, Long> getTargetRecreationsViewCounts();

    void incrementViewCountLast7Days(Long id);

    Long getTotalViewCountLast7Days(Long id);

    List<Long> getAllFlowIdsToUpdateViewCountLast7Days();
}
