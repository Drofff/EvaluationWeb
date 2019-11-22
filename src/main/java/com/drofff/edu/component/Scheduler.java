package com.drofff.edu.component;

import com.drofff.edu.entity.Test;
import com.drofff.edu.repository.TestRepository;
import com.drofff.edu.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
public class Scheduler {

    private final NotificationService notificationService;
    private final TestRepository testRepository;

    @Autowired
    public Scheduler(NotificationService notificationService, TestRepository testRepository) {
        this.notificationService = notificationService;
        this.testRepository = testRepository;
    }

    @Scheduled(cron = "0 0 13 * * ?")
    public void sendMailNotifications() {
        notificationService.notifyAboutTest();
    }

    @Scheduled(cron = "0 0 9/2 * * ?")
    public void scheduledTestActivation() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Test> testsToActive = testRepository.findNotActiveBetweenStartTimeAndDeadLine(currentDateTime);
        testsToActive.forEach(test -> {
            test.setActive(true);
            testRepository.save(test);
        });
    }

}
