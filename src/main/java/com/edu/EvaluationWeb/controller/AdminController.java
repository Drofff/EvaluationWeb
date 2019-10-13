package com.edu.EvaluationWeb.controller;

import com.edu.EvaluationWeb.constants.ModelConstants;
import com.edu.EvaluationWeb.dto.CreateUserDto;
import com.edu.EvaluationWeb.dto.ProfileAdminDto;
import com.edu.EvaluationWeb.dto.ProfileDto;
import com.edu.EvaluationWeb.entity.Group;
import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.exception.BaseException;
import com.edu.EvaluationWeb.mapper.ProfileMapper;
import com.edu.EvaluationWeb.service.AdminService;
import com.edu.EvaluationWeb.service.FilesService;
import com.edu.EvaluationWeb.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final ProfileMapper profileMapper;
    private final ValidationService validationService;

    private final String CURRENT_PAGE_PARAM = "current_page";
    private final String TOTAL_PAGES_COUNT_PARAM = "total_pages";

    @Autowired
    public AdminController(AdminService adminService, ProfileMapper profileMapper,
                           ValidationService validationService) {
        this.adminService = adminService;
        this.profileMapper = profileMapper;
        this.validationService = validationService;
    }

    @GetMapping("/groups")
    public String getAllGroups(Optional<String> name, Optional<Integer> page, Model model) {
        Page<Group> groups = adminService.getAllGroups(name, page.orElse(0));
        model.addAttribute("groups", groups.getContent());
        model.addAttribute(CURRENT_PAGE_PARAM, page.orElse(0));
        model.addAttribute(TOTAL_PAGES_COUNT_PARAM, groups.getTotalPages());
        name.ifPresent(n -> model.addAttribute("oldName", n));
        return "adminGroups";
    }

    @PostMapping("/groups")
    public String addNewGroup(String name, Model model) {
        try {
            adminService.addGroup(name);
        } catch(BaseException e) {
            model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
            return "errorPage";
        }
        return "redirect:/admin/groups";
    }

    @GetMapping("/manage/groups/{id}")
    public String managerGroupPage(@PathVariable Long id, Model model) {
        try {
            Group group = adminService.getGroupById(id);
            adminService.loadGroupPhotos(group);
            model.addAttribute("group", group);
            List<Profile> teachers = adminService.getAllTeachers();
            List<Profile> users = adminService.getAllUsers();
            model.addAttribute("teachers", teachers.stream()
                    .filter(teacher -> !group.getTeachers().contains(teacher))
                    .collect(Collectors.toList()));
            model.addAttribute("members", users.stream()
                    .filter(member -> !member.getGroup().getId().equals(id))
                    .collect(Collectors.toList()));
        } catch(BaseException e) {
            model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
        }
        return "manageGroupPage";
    }

    @PostMapping("/manage/{groupId}/add-teachers")
    public String addTeacherToGroup(@PathVariable Long groupId, Long id, Model model) {
        try {
            adminService.addTeacherToGroup(id, groupId);
        } catch(BaseException e) {
            model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
            return "errorPage";
        }
        return "redirect:/admin/manage/groups/" + groupId;
    }

    @PostMapping("/manage/{groupId}/add-members")
    public String addMemberToGroup(@PathVariable Long groupId, Long id, Model model) {
        try {
            adminService.addMemberToGroup(id, groupId);
        } catch(BaseException e) {
            model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
            return "errorPage";
        }
        return "redirect:/admin/manage/groups/" + groupId;
    }

    @GetMapping("/manage/{groupId}/remove-teachers/{teacherId}")
    public String removeTeacherFromGroup(@PathVariable Long groupId, @PathVariable Long teacherId,
                                         Model model) {
        try {
            adminService.removeTeacherFromGroup(teacherId, groupId);
        } catch(BaseException e) {
            model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
            return "errorPage";
        }
        return "redirect:/admin/manage/groups/" + groupId;
    }

    @GetMapping("/users")
    public String getAllUsers(Optional<String> name, @RequestParam(required = false) Set<String> groups,
                              @RequestParam(required = false) Integer page, Model model) {
        try {
            Integer pageNumber = Optional.ofNullable(page).orElse(0);
            Optional<Set<String>> selectedGroups = Optional.ofNullable(groups);
            Page<Profile> users = adminService.getAllUsers(name, selectedGroups, pageNumber);
            model.addAttribute(CURRENT_PAGE_PARAM, pageNumber);
            model.addAttribute(TOTAL_PAGES_COUNT_PARAM, users.getTotalPages());
            model.addAttribute("users", users.getContent().stream()
                    .map(this::toProfileAdminDto)
                    .collect(Collectors.toList()));
            model.addAttribute("groups", adminService.getAllGroups(groups));
            name.ifPresent(n -> model.addAttribute("oldName", n));
        } catch(BaseException e) {
            model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
        }
        return "adminUsers";
    }

    @PostMapping("/users")
    public String createUser(CreateUserDto userDto, Model model) {
        try {
            Map<String, String> errors = validationService.validate(userDto);
            if(errors.isEmpty()) {
                adminService.createAccount(userDto.getEmail(), userDto.getGroupId(),
                        userDto.getPosition(), userDto.getTeacher());
            } else {
                model.mergeAttributes(errors);
                model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, "Invalid data provided");
            }
        } catch(BaseException e) {
            model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
        }
        return "adminUsers";
    }

    private ProfileAdminDto toProfileAdminDto(Profile profile) {
        ProfileDto profileDto = profileMapper.toDto(profile);
        ProfileAdminDto profileAdminDto = new ProfileAdminDto(profileDto);
        Boolean isTeacher = profile.getUserId().isTeacher();
        profileAdminDto.setTeacher(isTeacher);
        profileAdminDto.setProfileId(profile.getId());
        profileAdminDto.setUserId(profile.getUserId().getId());
        return profileAdminDto;
    }

}
