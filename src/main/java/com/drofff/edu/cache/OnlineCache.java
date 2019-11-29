package com.drofff.edu.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.drofff.edu.entity.User;
import com.drofff.edu.exception.CacheException;

@Component
public class OnlineCache extends Cache<Boolean> {

	private static final Integer EXPIRE_AFTER_TIME = 1;
	private static final TimeUnit EXPIRE_AFTER_TIME_UNIT = TimeUnit.MINUTES;

	public void setOnline(User user) {
		save(user.getUsername(), true);
	}

	public boolean isOnline(User user) {
		try {
			return load(user.getUsername());
		} catch(CacheException e) {
			return false;
		}
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
