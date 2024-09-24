package com.avab.avab.redis.service;

import java.util.List;
import java.util.Map;

public interface FlowViewCountService {

    void incrementViewCount(Long id);

    void incrementViewCountLast7Days(Long id);

    Long getTotalViewCountLast7Days(Long id);

    List<Long> getAllFlowIdsToUpdateViewCountLast7Days();

    Map<Long, Long> getTargetFlowsViewCounts();
}
