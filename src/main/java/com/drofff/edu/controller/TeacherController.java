package com.drofff.edu.controller;

import com.drofff.edu.component.UserContext;
import com.drofff.edu.dto.CreateLessonDto;
import com.drofff.edu.dto.EditLessonDto;
import com.drofff.edu.entity.Lesson;
import com.drofff.edu.entity.User;
import com.drofff.edu.mapper.CreateLessonDtoMapper;
import com.drofff.edu.mapper.EditLessonDtoMapper;
import com.drofff.edu.repository.GroupRepository;
import com.drofff.edu.repository.LessonsRepository;
import com.drofff.edu.repository.ProfileRepository;
import com.drofff.edu.repository.SubjectRepository;
import com.drofff.edu.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
@PreAuthorize("hasAuthority('TEACHER')")
public class TeacherController {

    private final LessonsRepository lessonsRepository;
    private final LessonService lessonService;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final ProfileRepository profileRepository;
    private final EditLessonDtoMapper editLessonDtoMapper;
    private final CreateLessonDtoMapper createLessonDtoMapper;
    private final UserContext userContext;

    public static final String MESSAGE_SUCCESS = "messageSuccess";
    public static final String MESSAGE_ERROR = "messageError";

    @Autowired
    public TeacherController(LessonsRepository lessonsRepository, LessonService lessonService,
                             GroupRepository groupRepository, SubjectRepository subjectRepository,
                             ProfileRepository profileRepository, EditLessonDtoMapper editLessonDtoMapper,
                             CreateLessonDtoMapper createLessonDtoMapper, UserContext userContext) {
        this.lessonsRepository = lessonsRepository;
        this.lessonService = lessonService;
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;
        this.profileRepository = profileRepository;
        this.editLessonDtoMapper = editLessonDtoMapper;
        this.createLessonDtoMapper = createLessonDtoMapper;
        this.userContext = userContext;
    }


    @PostMapping("/delete")
    public String deleteLesson(@RequestParam String lessonId, HttpServletRequest request) {
        try {
            lessonsRepository.deleteById(Long.parseLong(lessonId));
        } catch (Exception e) {
            return "errorPage";
        }

        String referer = request.getHeader("referer");

        if (referer == null) {
            return "redirect:/";
        }

        return "redirect:" + URI.create(referer).getPath();
    }

    @RequestMapping("/edit/{lessonId}")
    public String editLessonPage(@PathVariable String lessonId, Model model) {

        try {

            Lesson lesson = lessonsRepository.findById(Long.parseLong(lessonId)).get();

            model = lessonService.getBasicEditModel(model, lesson);

        } catch (Exception e) {
            e.printStackTrace();
            return "errorPage";
        }

        return "lessonEditPage";

    }

    @PostMapping("/edit")
    public String editLesson(Model model, EditLessonDto lesson) {

        if(lesson == null || lesson.getId() == null || !lessonsRepository.findById(lesson.getId()).isPresent()) {
            return "redirect:/";
        }

        Lesson editData = editLessonDtoMapper.toEntity(lesson);

        Map<String, String> result = lessonService.editLesson(editData);

        if(result.size() > 0) {
            model.addAttribute(MESSAGE_ERROR, "Wrong data was provided. Please fix input data due to requirements");
            model.mergeAttributes(result);
        } else {
            model.addAttribute(MESSAGE_SUCCESS, "Successfully updated lesson");
        }

        model = lessonService.getBasicEditModel(model, editData);
        return "lessonEditPage";
    }

    @RequestMapping("/manager")
    public String lessonsManager(Model model, @RequestParam(required = false) String msg, @RequestParam(required = false) String msgErr) {

        if (msg != null && msg.equals("saved")) {
            model.addAttribute("message", "Successfully added new lessons");
        } else if (msgErr != null) {
            model.addAttribute("msgErr", msgErr);
        }

        User user = userContext.getCurrentUser();

        model.addAttribute("groups", groupRepository.findAll());
        model.addAttribute("subjects", subjectRepository.findAll().stream()
                .filter(x -> x.getTeacher().getUsername().equals(user.getUsername()))
                .collect(Collectors.toList()));

        return "lessonsManagerPage";
    }

    @PostMapping("/manager/add")
    public String addLessons(CreateLessonDto createLessonDto) {
        return lessonService.scheduleLesson(createLessonDtoMapper.toEntity(createLessonDto), createLessonDto.getDates(),
                                            createLessonDto.getTime());
    }

    @RequestMapping("/manager/edit")
    public String editLessons(Model model) {

        User user = userContext.getCurrentUser();
        Map<String, Lesson> lessons = new LinkedHashMap<>();

        lessonsRepository.findAll().stream()
                .filter(x -> x.getTeacher().getUserId().getUsername().equals(user.getUsername()))
                .sorted((x, y) -> {

                    ZoneOffset zoneOffset = ZoneOffset.from(ZonedDateTime.now());

                    Long xSec = x.getDateTime().toEpochSecond(zoneOffset);
                    Long ySec = y.getDateTime().toEpochSecond(zoneOffset);
                    Long diffSec = xSec - ySec;

                    return diffSec.intValue();
                }
                    ).forEach(x -> lessons.put(x.getDateTime().format(DateTimeFormatter.ofPattern("uuuu'/'MM'/'dd' 'HH:mm' 'EEEE")), x));

        model.addAttribute("lessons", lessons);

        return "lessonsManagerAddPage";
    }

}
