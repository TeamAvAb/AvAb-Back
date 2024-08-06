package com.avab.avab.redis.service;

import java.util.List;

public interface RecreationViewCountService {

    void incrementViewCount(Long recreationId);

    Long getViewCount(Long recreationId);

    List<Long> getAllRecreationIds();

    void incrementViewCountLast7Days(Long id);

    Long getTotalViewCountLast7Days(Long id);

    List<Long> getAllFlowIdsToUpdateViewCountLast7Days();
}
