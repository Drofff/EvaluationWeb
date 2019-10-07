package com.edu.EvaluationWeb.service;

import com.edu.EvaluationWeb.component.UserContext;
import com.edu.EvaluationWeb.entity.Lesson;
import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.entity.User;
import com.edu.EvaluationWeb.mapper.LessonMapper;
import com.edu.EvaluationWeb.repository.GroupRepository;
import com.edu.EvaluationWeb.repository.LessonsRepository;
import com.edu.EvaluationWeb.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LessonService {

    private final LessonsRepository lessonsRepository;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final ValidationService validationService;
    private final CalendarService calendarService;
    private final LessonMapper lessonMapper;
    private final UserContext userContext;

    @Autowired
    public LessonService(LessonsRepository lessonsRepository, GroupRepository groupRepository,
                         SubjectRepository subjectRepository, ValidationService validationService,
                         CalendarService calendarService, LessonMapper lessonMapper, UserContext userContext) {
        this.lessonsRepository = lessonsRepository;
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;

        this.validationService = validationService;
        this.calendarService = calendarService;
        this.lessonMapper = lessonMapper;
        this.userContext = userContext;
    }

    public Map<String, String> editLesson(Lesson lesson) {
        Optional<Lesson> optionalLesson = lessonsRepository.findById(lesson.getId());
        if(!optionalLesson.isPresent()) {
            return Collections.emptyMap();
        }

        Map<String, String> validationResult = validationService.validate(lesson);
        if(validationResult.size() > 0) {
            return validationResult;
        }

        User user = userContext.getCurrentUser();
        Profile profile = user.getProfile();
        lessonsRepository.delete(optionalLesson.get());

        String checkDateResult = calendarService.isDateTimeFree(lesson.getGroupId(), lesson.getDateTime(),
                                                (int) lesson.getDuration().toMinutes());

        if(!checkDateResult.equals(CalendarService.FREE_STATUS)) {
            validationResult.put("dateTimeError", checkDateResult);
            lessonsRepository.save(optionalLesson.get());
            return validationResult;
        }

        lesson.setTeacher(profile);
        lessonsRepository.save(lesson);

        return validationResult;
    }

    public Model getBasicEditModel(Model model, Lesson lesson) {
        User user = userContext.getCurrentUser();
        model.addAttribute("lesson", lessonMapper.toDto(lesson));
        model.addAttribute("groups", groupRepository.findAll());
        model.addAttribute("subjects", subjectRepository.findAll().stream().filter(x -> x.getTeacher().getUsername().equals(user.getUsername())).collect(Collectors.toList()));
        return model;
    }

    public String scheduleLesson(Lesson lesson, String dates, String time) {

        User user = userContext.getCurrentUser();
        lesson.setTeacher(user.getProfile());
        lesson.setTest(false);

        List<LocalDate> localDateList = calendarService.parseDates(dates);
        LocalTime localTime = LocalTime.parse(time);
        LocalDateTime now = LocalDateTime.now();

        List<Lesson> lessonsToSave = new ArrayList<>();
        final String ERROR_REDIRECT_BASE_PATH = "redirect:/teacher/manager?msgErr=";

        for(LocalDate date : localDateList) {

            LocalDateTime selectedTime = date.atTime(localTime);

            if(!selectedTime.isAfter(now)) {
                return ERROR_REDIRECT_BASE_PATH + "Please provide valid date (" +
                        selectedTime.format(CalendarService.DISPLAY_FORMATTER) + ")";
            }

            String dateTimeSlotResult = calendarService.isDateTimeFree(lesson.getGroupId(), selectedTime,
                    (int) lesson.getDuration().toMinutes());

            if(dateTimeSlotResult.equals(CalendarService.FREE_STATUS)) {
                Lesson newLesson = lesson.clone();
                newLesson.setDateTime(selectedTime);
                lessonsToSave.add(newLesson);
            } else {
                return ERROR_REDIRECT_BASE_PATH + dateTimeSlotResult + " ("
                        + selectedTime.format(CalendarService.DISPLAY_FORMATTER) + ")";
            }

        }

        Map<String, String> result = validationService.validate(lesson);

        if(result.size() > 0) {
            result.forEach((x,y) -> System.out.println(x + ":" + y));
            return ERROR_REDIRECT_BASE_PATH + "Validation error";
        }

        lessonsToSave.forEach(lessonsRepository::save);

        return "redirect:/teacher/manager?msg=saved";
    }

}
