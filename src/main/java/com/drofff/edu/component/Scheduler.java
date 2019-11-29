package com.drofff.edu.component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.drofff.edu.cache.AttendanceCache;
import com.drofff.edu.entity.Attendance;
import com.drofff.edu.entity.Test;
import com.drofff.edu.entity.User;
import com.drofff.edu.repository.AttendanceRepository;
import com.drofff.edu.repository.TestRepository;
import com.drofff.edu.repository.UserRepository;
import com.drofff.edu.service.NotificationService;

@Component
@EnableScheduling
public class Scheduler {

	private static final Logger LOG = LoggerFactory.getLogger(Scheduler.class);

	private static final Integer ATTENDANCE_CACHE_CLEAR_INTERVAL = 10 * 60;

    private final NotificationService notificationService;
    private final TestRepository testRepository;
    private final AttendanceCache attendanceCache;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    @Autowired
    public Scheduler(NotificationService notificationService, TestRepository testRepository,
                     AttendanceCache attendanceCache, AttendanceRepository attendanceRepository,
                     UserRepository userRepository) {
        this.notificationService = notificationService;
        this.testRepository = testRepository;
	    this.attendanceCache = attendanceCache;
	    this.attendanceRepository = attendanceRepository;
	    this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 13 * * ?")
    public void sendMailNotifications() {
        LOG.info("Sending mail notifications about tests");
    	notificationService.notifyAboutTest();
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void scheduledTestActivation() {
    	LOG.info("Looking for tests to activate");
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Test> testsToActive = testRepository.findNotActiveBetweenStartTimeAndDeadLine(currentDateTime);
        testsToActive.forEach(test -> {
        	LOG.info("Activating test id=[{}], startTime=[{}]", test.getId(), test.getStartTime());
            test.setActive(true);
            testRepository.save(test);
        });
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void cleanAttendanceCache() {
    	LOG.info("Cleaning attendance cache");
	    Map<String, LocalDateTime> cache = attendanceCache.loadAll();
	    LOG.info("{} entries in attendance cache", cache.size());
	    cache.forEach(this::removeAttendanceFromCacheIfNeeded);
    }

    private void removeAttendanceFromCacheIfNeeded(String username, LocalDateTime lastRequestTime) {
    	if(isAttendanceOver(lastRequestTime)) {
    		LOG.info("Removing attendance entry for user with username=[{}] from attendance cache", username);
    		Attendance attendance = buildAttendance(username, lastRequestTime);
    		attendanceRepository.save(attendance);
    		attendanceCache.invalidateByUsername(username);
	    }
    }

    private boolean isAttendanceOver(LocalDateTime lastRequestTime) {
    	LocalDateTime now = LocalDateTime.now();
	    Duration interval = Duration.between(lastRequestTime, now);
	    return interval.getSeconds() > ATTENDANCE_CACHE_CLEAR_INTERVAL;
    }

    private Attendance buildAttendance(String username, LocalDateTime lastAttendance) {
	    User user = userRepository.findByUsername(username);
	    return new Attendance(user.getId(), lastAttendance);
    }

}