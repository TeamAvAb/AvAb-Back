package com.avab.avab.redis.service;

import java.util.List;

public interface FlowViewCountService {

    void incrementViewCount(Long id);

    Long getViewCount(Long id);

    List<Long> getAllFlowIds();
}
