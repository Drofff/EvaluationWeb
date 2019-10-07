package com.edu.EvaluationWeb.service;

import com.edu.EvaluationWeb.entity.Lesson;
import com.edu.EvaluationWeb.repository.LessonsRepository;
import com.edu.EvaluationWeb.utils.MailUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final LessonsRepository lessonsRepository;
    private final MailService mailService;

    private static final Integer NOTIFY_DAYS_BEFORE_TEST = 3;

    private static final String DATE_MARKER = "DATE";
    private static final String SUBJECT_MARKER = "SUBJECT";
    private static final String TEACHER_MARKER = "TEACHER";
    private static final String ROOM_MARKER = "ROOM";
    private static final String DESCRIPTION_MARKER = "DESCRIPTION";

    @Value("${mail.message.test}")
    private String testNotificationMessage;

    private final DateTimeFormatter dateTimeFormatter;

    @Autowired
    public NotificationService(LessonsRepository lessonsRepository, MailService mailService) {
        this.lessonsRepository = lessonsRepository;
        this.mailService = mailService;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("'on 'EEEE' 'dd' of 'MMMM");
    }

    @Transactional
    public void notifyAboutTest() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<Lesson> lessons = lessonsRepository.findByDateTimeBetweenAndIsTest(today.plusDays(NOTIFY_DAYS_BEFORE_TEST).atStartOfDay(),
                today.plusDays(NOTIFY_DAYS_BEFORE_TEST + 1).atStartOfDay(), true);

        lessons.addAll(lessonsRepository.findByDateTimeBetweenAndIsTest(tomorrow.atStartOfDay(), tomorrow.plusDays(1).atStartOfDay(), true));

        lessons.forEach(x -> {
            Map<String, String> markerValues = new HashMap<>();
            markerValues.put(DATE_MARKER, x.getDateTime().toLocalDate().isEqual(tomorrow) ? "tomorrow" : x.getDateTime().format(dateTimeFormatter));
            markerValues.put(SUBJECT_MARKER, x.getTitle());
            markerValues.put(TEACHER_MARKER, x.getTeacher().getFirstName() + " " + x.getTeacher().getLastName());
            markerValues.put(ROOM_MARKER, x.getRoom());
            markerValues.put(DESCRIPTION_MARKER, x.getDescription());

            Hibernate.initialize(x.getGroupId().getMembers());

            mailService.sendEmail("Notification about test",
                    MailUtils.fillMarkers(markerValues.entrySet().iterator(), testNotificationMessage),
                    x.getGroupId().getMembers().stream()
                            .map(y -> y.getUserId().getUsername())
                            .collect(Collectors.toList()).toArray(new String [] {}));
        });

    }

}
