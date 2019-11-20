package com.edu.EvaluationWeb.service;

import com.edu.EvaluationWeb.component.UserContext;
import com.edu.EvaluationWeb.entity.Group;
import com.edu.EvaluationWeb.entity.Lesson;
import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.entity.User;
import com.edu.EvaluationWeb.mapper.LessonMapper;
import com.edu.EvaluationWeb.repository.GroupRepository;
import com.edu.EvaluationWeb.repository.LessonsRepository;
import com.edu.EvaluationWeb.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    @Autowired
    LessonsRepository lessonsRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    LessonMapper lessonMapper;

    @Autowired
    UserContext userContext;

    public static final String FREE_STATUS = "Free";
    public static final String BUSY_MESSAGE = "Sorry, but it seems like there are some other lessons on this date ";

    public static final DateTimeFormatter PARSING_FORMATTER = DateTimeFormatter.ofPattern("MM'/'dd'/'uuuu");
    public static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("uuuu'/'MM'/'dd' 'HH:mm");

    public Map<Integer, List<Lesson>> lessonsFromWeek(int week, User user) {

        if (week < 0) {
            return new HashMap<>();
        }

        LocalDateTime dateTime = LocalDateTime.now();

        if (week == 0) {

            while (!dateTime.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
                dateTime = dateTime.minus(1, ChronoUnit.DAYS);
            }

        } else {

            int count = 0;

            while (week != count) {

                dateTime = dateTime.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                count++;

            }

        }

        List <Lesson> lessons = lessonsRepository.findByDate(dateTime.toLocalDate(), dateTime.toLocalDate().plusDays(6));

        if (user.isTeacher()) {

            lessons = lessons.stream().filter(x -> x.getTeacher().getUserId().getUsername().equals(user.getUsername())).collect(Collectors.toList());

        } else if ( ! user.isAdmin() ) {

            Group userGroup = profileRepository.findByUserId(user).getGroup();

            lessons = lessons.stream().filter(x -> x.getId().equals(userGroup.getId())).collect(Collectors.toList());

        }

        if (lessons == null) {
            return new HashMap<>();
        }

        Map<Integer, List<Lesson>> lessonsMap = new LinkedHashMap<>();

        lessons.forEach(x -> {

            Integer day = x.getDateTime().getDayOfWeek().getValue();

            if (lessonsMap.containsKey(day)) {

                lessonsMap.get(day).add(x);

            } else {

                List<Lesson> lsns = new LinkedList<>();

                lsns.add(x);

                lessonsMap.put(day, lsns);

            }

        });

        return lessonsMap;
    }


    public Model getMainPageModel(Model model, Integer dayToDisplay, Integer weekToDisplay) {
        User user = userContext.getCurrentUser();
        Map<Integer, List<Lesson>> lessonsForWithWeek = lessonsFromWeek(weekToDisplay, user);

        if (dayToDisplay == DayOfWeek.SUNDAY.getValue() || dayToDisplay == DayOfWeek.SATURDAY.getValue()) {
            model.addAttribute("vacation", true);
        } else {
            List<Lesson> lessons = lessonsForWithWeek.get(dayToDisplay);
            model.addAttribute("lessons", lessons != null ? lessonsForWithWeek.get(dayToDisplay)
                                    .stream()
                                        .map(lessonMapper::toDto)
                                        .collect(Collectors.toList()) : null);
        }

        //mark days with tests

        if (lessonsForWithWeek != null) {

            for (int i = 1; i < 6; i++) {

                if (lessonsForWithWeek.containsKey(i)) {

                    for (Lesson lesson : lessonsForWithWeek.get(i)) {

                        if (lesson.getTest() != null && lesson.getTest()) {

                            model.addAttribute("test_at_" + lesson.getDateTime().getDayOfWeek().getValue(), true);
                            break;

                        }

                    }

                }

            }

        }

        //get today's date

        LocalDate todayDate = LocalDate.now();
        Integer today = todayDate.getDayOfWeek().getValue();

        model.addAttribute("day", dayToDisplay);
        model.addAttribute("week", weekToDisplay);
        model.addAttribute("today", today);

        //get date of selected day

        LocalDate dateOfSelectedDay = null;

        if (dayToDisplay == today) {

            dateOfSelectedDay = todayDate;

        } else if (dayToDisplay > today) {

            dateOfSelectedDay = todayDate.plus(dayToDisplay - today, ChronoUnit.DAYS);

        } else {

            dateOfSelectedDay = todayDate.minus(today - dayToDisplay, ChronoUnit.DAYS);

        }

        if (dateOfSelectedDay != null && weekToDisplay > 0) {

            dateOfSelectedDay = dateOfSelectedDay.plusWeeks(weekToDisplay);

        }

        model.addAttribute("todayDate", dateOfSelectedDay);

        return model;
    }

    public List<LocalDate> parseDates(String dates) throws DateTimeParseException {
        dates = dates.trim();
        String[] datesArray = dates.split(",");

        if (datesArray.length > 1) {
            return Arrays.stream(datesArray)
                    .map(x -> LocalDate.parse(x.trim(), PARSING_FORMATTER))
                    .collect(Collectors.toList());
        }

        return Collections.singletonList(LocalDate.parse(dates, PARSING_FORMATTER));
    }

    public String isDateTimeFree(Group group, LocalDateTime scheduledTime, Integer duration) {

        User user = userContext.getCurrentUser();
        Profile teacherProfile = user.getProfile();
        LocalDateTime finishTime = scheduledTime.plusMinutes(duration);

        Integer inTimeOfLessonCount = lessonsRepository.countByDate(scheduledTime, finishTime, group, teacherProfile);
        Lesson leftNeighbour = lessonsRepository.findFirstLeftNeighbour(scheduledTime, group.getId(), teacherProfile.getId());

        return inTimeOfLessonCount > 0 || leftNeighbour != null && leftNeighbour.getDateTime().isAfter(scheduledTime) ? BUSY_MESSAGE : FREE_STATUS;
    }


}
