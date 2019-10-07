package com.edu.EvaluationWeb.controller;

import com.edu.EvaluationWeb.dto.CreateUserDto;
import com.edu.EvaluationWeb.dto.ProfileAdminDto;
import com.edu.EvaluationWeb.dto.ProfileDto;
import com.edu.EvaluationWeb.entity.Group;
import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.exception.BaseException;
import com.edu.EvaluationWeb.mapper.ProfileMapper;
import com.edu.EvaluationWeb.service.AdminService;
import com.edu.EvaluationWeb.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("ADMIN")
public class AdminController {

    private final AdminService adminService;
    private final ProfileMapper profileMapper;
    private final ValidationService validationService;

    private final String CURRENT_PAGE_PARAM = "current_page";
    private final String TOTAL_PAGES_COUNT_PARAM = "total_pages";
    private final String ERROR_MESSAGE_PARAM = "error_message";

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
        return "adminGroups";
    }

    @PostMapping("/groups")
    public String addNewGroup(String name, Model model) {
        try {
            adminService.addGroup(name);
        } catch(BaseException e) {
            model.addAttribute(ERROR_MESSAGE_PARAM, e.getMessage());
        }
        return "adminGroups";
    }

    @PostMapping("/user-group")
    public String editGroupForUser(Long userId, Long groupId, Model model) {
        try {
            adminService.changeGroupForUser(userId, groupId);
        } catch(BaseException e) {
            model.addAttribute(ERROR_MESSAGE_PARAM, e.getMessage());
        }
        return "adminGroups";
    }

    @GetMapping("/users")
    public String getAllUsers(Optional<String> name, Optional<Set<String>> groups,
                              Optional<Integer> page, Model model) {
        Page<Profile> users = adminService.getAllUsers(name, groups, page.orElse(0));
        model.addAttribute(CURRENT_PAGE_PARAM, page.orElse(0));
        model.addAttribute(TOTAL_PAGES_COUNT_PARAM, users.getTotalPages());
        model.addAttribute("users", users.getContent().stream()
                .map(this::toProfileAdminDto)
                .collect(Collectors.toList()));
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
                model.addAttribute(ERROR_MESSAGE_PARAM, "Invalid data provided");
            }
        } catch(BaseException e) {
            model.addAttribute(ERROR_MESSAGE_PARAM, e.getMessage());
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
