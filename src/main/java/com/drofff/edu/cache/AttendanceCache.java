package com.drofff.edu.cache;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.drofff.edu.entity.User;

@Component
public class AttendanceCache extends Cache<LocalDateTime> {

	private static final TimeUnit EXPIRE_AFTER_TIME_UNIT = TimeUnit.MINUTES;
	private static final Integer EXPIRE_AFTER_TIME = 20;

	public Map<String, LocalDateTime> loadAll() {
		com.github.benmanes.caffeine.cache.Cache<String, LocalDateTime> cache = super.getCaffeineCache();
		ConcurrentMap<String, LocalDateTime> cacheMap = cache.asMap();
		return new HashMap<>(cacheMap);
	}

	public void refreshAttendance(User user) {
		LocalDateTime now = LocalDateTime.now();
		save(user.getUsername(), now);
	}

	public void invalidateByUsername(String username) {
		getCaffeineCache().invalidate(username);
	}

	@Override
	protected Integer expireAfterValue() {
		return EXPIRE_AFTER_TIME;
	}

	@Override
	protected TimeUnit expireAfterTimeUnit() {
		return EXPIRE_AFTER_TIME_UNIT;
	}

}
