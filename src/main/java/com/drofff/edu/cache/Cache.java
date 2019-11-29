package com.drofff.edu.cache;

import com.drofff.edu.exception.CacheException;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Cache<T> {

    private static final Integer EXPIRE_TIME = 1;
    private static final TimeUnit EXPIRE_TIME_UNIT = TimeUnit.HOURS;

    private com.github.benmanes.caffeine.cache.Cache<String, T> caffeineCache = Caffeine.newBuilder()
            .expireAfterWrite(expireAfterValue(), expireAfterTimeUnit())
            .build();

    public void save(String key, T value) {
        caffeineCache.put(key, value);
    }

    public T load(String key) {
        T value = caffeineCache.getIfPresent(key);
        if(Objects.isNull(value)) {
            throw new CacheException("No data for such key");
        }
        return value;
    }

    public void remove(String key) {
        caffeineCache.invalidate(key);
    }

    protected Integer expireAfterValue() {
        return EXPIRE_TIME;
    }

    protected TimeUnit expireAfterTimeUnit() {
    	return EXPIRE_TIME_UNIT;
    }

    protected com.github.benmanes.caffeine.cache.Cache<String, T> getCaffeineCache() {
    	return caffeineCache;
    }

}
