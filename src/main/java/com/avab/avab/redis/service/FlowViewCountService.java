package com.avab.avab.redis.service;

import java.util.List;

public interface FlowViewCountService {

    void incrementViewCount(Long id);

    Long getViewCount(Long id);

    List<Long> getAllFlowIds();

    void incrementViewCountLast7Days(Long id);

    Long getTotalViewCountLast7Days(Long id);

    List<Long> getAllFlowIdsToUpdateViewCountLast7Days();
}
