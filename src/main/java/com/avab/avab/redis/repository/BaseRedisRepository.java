package com.avab.avab.redis.repository;

abstract class BaseRedisRepository<ID_TYPE> {
    protected abstract String createKey(ID_TYPE id);

    protected abstract ID_TYPE extractId(String key);
}
