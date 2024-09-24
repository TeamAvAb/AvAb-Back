package com.avab.avab.redis.service;

import java.util.List;
import java.util.Map;

public interface RecreationViewCountService {

    void incrementViewCount(Long recreationId);

    Long getViewCount(Long recreationId);

    Map<Long, Long> getViewCountsByIds(List<Long> recreationIds);

    List<Long> getAllRecreationIds();

    void incrementViewCountLast7Days(Long id);

    Long getTotalViewCountLast7Days(Long id);

    List<Long> getAllFlowIdsToUpdateViewCountLast7Days();
}
