package com.avab.avab.redis.repository;

import java.util.List;

public interface RecreationViewCountRepository {

    void incrementViewCount(String key);

    void createViewCount(String key);

    String getViewCount(String key);

    List<String> getAllRecreationIds();
}
