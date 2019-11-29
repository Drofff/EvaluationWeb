package com.drofff.edu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.drofff.edu.constants.ModelConstants;
import com.drofff.edu.dto.GroupOptionDto;
import com.drofff.edu.dto.ProfileDto;
import com.drofff.edu.dto.StudentDto;
import com.drofff.edu.entity.Group;
import com.drofff.edu.entity.Profile;
import com.drofff.edu.entity.Subject;
import com.drofff.edu.exception.BaseException;
import com.drofff.edu.mapper.ProfileMapper;
import com.drofff.edu.service.AttendanceService;
import com.drofff.edu.service.FilesService;
import com.drofff.edu.service.StudentService;
import com.drofff.edu.service.SubjectsService;

@Controller
@RequestMapping("/students")
@PreAuthorize("hasAuthority('TEACHER')")
public class StudentsSearchController {

    private final StudentService studentService;
    private final FilesService filesService;
    private final ProfileMapper profileMapper;
    private final AttendanceService attendanceService;
    private final SubjectsService subjectsService;

    @Autowired
    public StudentsSearchController(StudentService studentService, FilesService filesService,
                                    ProfileMapper profileMapper, AttendanceService attendanceService,
                                    SubjectsService subjectsService) {
        this.studentService = studentService;
        this.filesService = filesService;
	    this.profileMapper = profileMapper;
	    this.attendanceService = attendanceService;
	    this.subjectsService = subjectsService;
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

    @GetMapping("/{id}")
	public String getStudentById(@PathVariable Long id, Model model) {
    	try {
		    Profile student = studentService.getStudentById(id);
		    ProfileDto profileDto = profileMapper.toDto(student);
		    StudentDto studentDto = new StudentDto(profileDto);
		    String userStatus = attendanceService.getStatusOfUser(student.getUserId());
		    studentDto.setStatus(userStatus);
		    studentDto.setId(student.getId());
		    model.addAttribute("student", studentDto);
		    List<Subject> subjects = subjectsService.getMySubjects();
		    if(!subjects.isEmpty()) {
			    model.addAttribute("subject", subjects.get(0));
		    }
	    } catch(BaseException e) {
    		model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
	    }
    	return "viewStudentPage";
    }

}
