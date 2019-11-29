package com.drofff.edu.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.edu.cache.AttendanceCache;
import com.drofff.edu.cache.OnlineCache;
import com.drofff.edu.entity.Attendance;
import com.drofff.edu.entity.User;
import com.drofff.edu.exception.CacheException;
import com.drofff.edu.repository.AttendanceRepository;

@Service
public class AttendanceService {

	private static final String LAST_SEEN_AT_MESSAGE_PREFIX = "Last seen ";
	private static final String TODAY = "Today";
	private static final String YESTERDAY = "Yesterday";

	private static final String ONLINE_STATUS = "Online";
	private static final String OFFLINE_STATUS = "Offline";

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd' of 'LLLL");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("kk':'mm");

	private final OnlineCache onlineCache;
	private final AttendanceRepository attendanceRepository;
	private final AttendanceCache attendanceCache;

	@Autowired
	public AttendanceService(OnlineCache onlineCache, AttendanceRepository attendanceRepository,
	                         AttendanceCache attendanceCache) {
		this.onlineCache = onlineCache;
		this.attendanceRepository = attendanceRepository;
		this.attendanceCache = attendanceCache;
	}

	public String getStatusOfUser(User user) {
		if(!onlineCache.isOnline(user)) {
			Optional<String> lastSeenAtMessage = getLastSeenAtMessageForUserIfPossible(user);
			return lastSeenAtMessage.orElse(OFFLINE_STATUS);
		}
		return ONLINE_STATUS;
	}

	private Optional<String> getLastSeenAtMessageForUserIfPossible(User user) {
		Optional<LocalDateTime> lastAttendanceOptional = getLastAttendanceOfUserIfPresent(user);
		if(lastAttendanceOptional.isPresent()) {
			LocalDateTime lastAttendance = lastAttendanceOptional.get();
			String relativeDateTime = toRelativeDateTimeStr(lastAttendance);
			String message = LAST_SEEN_AT_MESSAGE_PREFIX + relativeDateTime;
			return Optional.of(message);
		}
		return Optional.empty();
	}

	private Optional<LocalDateTime> getLastAttendanceOfUserIfPresent(User user) {
		Optional<LocalDateTime> lastAttendanceFromCache = getLastAttendanceOfUserFromCacheIfPresent(user);
		if(lastAttendanceFromCache.isPresent()) {
			return lastAttendanceFromCache;
		}
		return attendanceRepository.findById(user.getId())
				.map(Attendance::getLastAttendance);
	}

	private Optional<LocalDateTime> getLastAttendanceOfUserFromCacheIfPresent(User user) {
		try {
			return Optional.of(attendanceCache.load(user.getUsername()));
		} catch(CacheException e) {
			return Optional.empty();
		}
	}

	private String toRelativeDateTimeStr(LocalDateTime localDateTime) {
		String dateStr = getRelativeDateStr(localDateTime.toLocalDate());
		String timeStr = localDateTime.format(TIME_FORMATTER);
		return dateStr + " at " + timeStr;
	}

	private String getRelativeDateStr(LocalDate localDate) {
		if(isToday(localDate)) {
			return TODAY;
		}
		if(isYesterday(localDate)) {
			return YESTERDAY;
		}
		return localDate.format(DATE_FORMATTER);
	}

	private boolean isToday(LocalDate localDate) {
		LocalDate now = LocalDate.now();
		return localDate.isEqual(now);
	}

	private boolean isYesterday(LocalDate localDate) {
		LocalDate now = LocalDate.now();
		return localDate.isEqual(now.minusDays(1));
	}

}