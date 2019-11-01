package com.edu.EvaluationWeb.controller;

import com.edu.EvaluationWeb.dto.GroupOptionDto;
import com.edu.EvaluationWeb.entity.Group;
import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.exception.BaseException;
import com.edu.EvaluationWeb.service.FilesService;
import com.edu.EvaluationWeb.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/students")
@PreAuthorize("hasAuthority('TEACHER')")
public class StudentsSearchController {

    private final StudentService studentService;
    private final FilesService filesService;

    @Autowired
    public StudentsSearchController(StudentService studentService, FilesService filesService) {
        this.studentService = studentService;
        this.filesService = filesService;
    }

    @GetMapping("/all")
    public String getMyStudents(Optional<Integer> page, Model model) {
        try {
            Page<Profile> myStudents = studentService.getMyStudents(page.orElse(0));
            List<Profile> studentsWithPhotos = myStudents.stream()
                    .peek(profile -> {
                        if(Objects.isNull(profile.getPhotoUrl())) {
                            return;
                        }
                        String base64Photo = filesService.loadPhoto(profile.getPhotoUrl());
                        profile.setPhotoUrl(base64Photo);
                    }).collect(Collectors.toList());
            Set<Group> myGroups = studentService.getMyGroups();
            model.addAttribute("students", studentsWithPhotos);
            model.addAttribute("groups", myGroups);
        } catch(BaseException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "studentsSearchPage";
    }

    @GetMapping("/search")
    public String searchInMyStudents(Optional<String> name, @RequestParam(value = "group_id", required = false) List<Long> groupIds,
                                     Optional<Integer> page, Model model) {
        try {
            groupIds = initIfNull(groupIds);
            Page<Profile> students = studentService.findByNameAndGroupIfPresent(name, groupIds, page.orElse(0));
            List<Profile> studentsWithPhotos = students.stream()
                    .peek(profile -> {
                        String base64Photo = filesService.loadPhoto(profile.getPhotoUrl());
                        profile.setPhotoUrl(base64Photo);
                    }).collect(Collectors.toList());
            Set<Group> myGroups = studentService.getMyGroups();
            Set<GroupOptionDto> groupOptionDtos = toGroupOptionDtoSet(myGroups);
            List<Long> finalGroupIds = groupIds;
            groupOptionDtos.forEach(groupOptionDto -> markIfSelected(groupOptionDto, finalGroupIds));
            model.addAttribute("students", studentsWithPhotos);
            model.addAttribute("groups", groupOptionDtos);
            model.addAttribute("oldName", name.orElse(""));
        } catch(BaseException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "studentsSearchPage";
    }

    private Set<GroupOptionDto> toGroupOptionDtoSet(Set<Group> groups) {
        return groups.stream()
                .map(GroupOptionDto::new)
                .collect(Collectors.toSet());
    }

    private void markIfSelected(GroupOptionDto optionDto, List<Long> selectedGroupsIds) {
        boolean isSelected = selectedGroupsIds.stream()
                .anyMatch(id -> optionDto.getId().equals(id));
        if(isSelected) {
            optionDto.setSelected(true);
        }
    }

    private List<Long> initIfNull(List<Long> groupIds) {
        if(Objects.isNull(groupIds)) {
            groupIds = new ArrayList<>();
        }
        return groupIds;
    }

}
