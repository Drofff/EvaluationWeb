package com.drofff.edu.cache;

import com.drofff.edu.exception.CacheException;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Cache<T> {

    private static final Integer EXPIRE_TIME_IN_HOURS = 1;

    private com.github.benmanes.caffeine.cache.Cache<String, T> cache = Caffeine.newBuilder()
            .expireAfterWrite(expireTimeInHours(), TimeUnit.HOURS)
            .build();

    public void save(String key, T value) {
        cache.put(key, value);
    }

    public T load(String key) {
        T value = cache.getIfPresent(key);
        if(Objects.isNull(value)) {
            throw new CacheException("No data for such key");
        }
        return value;
    }

    public void remove(String key) {
        cache.invalidate(key);
    }

    protected Integer expireTimeInHours() {
        return EXPIRE_TIME_IN_HOURS;
    }

}
