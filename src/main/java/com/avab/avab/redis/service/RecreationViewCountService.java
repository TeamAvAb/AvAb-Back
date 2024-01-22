package com.avab.avab.redis.service;

import java.util.List;

public interface RecreationViewCountService {

    void incrementViewCount(Long recreationId);

    Long getViewCount(Long recreationId);

    List<Long> getAllRecreationIds();
}
