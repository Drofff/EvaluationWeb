package com.drofff.edu.controller;

import com.drofff.edu.component.NodesComparator;
import com.drofff.edu.component.UserContext;
import com.drofff.edu.dto.EditProfileDto;
import com.drofff.edu.dto.ProfileDto;
import com.drofff.edu.dto.TeacherProfileDto;
import com.drofff.edu.entity.Group;
import com.drofff.edu.entity.Profile;
import com.drofff.edu.entity.StorageNode;
import com.drofff.edu.entity.Subject;
import com.drofff.edu.exception.BaseException;
import com.drofff.edu.mapper.ProfileMapper;
import com.drofff.edu.mapper.StorageNodeDtoMapper;
import com.drofff.edu.service.FilesService;
import com.drofff.edu.service.ProfileService;
import com.drofff.edu.service.StorageService;
import com.drofff.edu.utils.PathUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileMapper profileMapper;
    private final ProfileService profileService;
    private final StorageService storageService;
    private final UserContext userContext;
    private final StorageNodeDtoMapper storageNodeDtoMapper;
    private final FilesService filesService;

    private static final String VIEW_PROFILE_MAPPING_PATTERN = "/profile/view/(\\d+)/";

    @Autowired
    public ProfileController(ProfileMapper profileMapper, ProfileService profileService,
                             StorageService storageService, UserContext userContext,
                             StorageNodeDtoMapper storageNodeDtoMapper, FilesService filesService) {
        this.profileMapper = profileMapper;
        this.profileService = profileService;
        this.storageService = storageService;
        this.userContext = userContext;
        this.storageNodeDtoMapper = storageNodeDtoMapper;
	    this.filesService = filesService;
    }

    @GetMapping
    public String myProfile(Model model) {

        Profile profile = profileService.getCurrentProfile();

        if(profile != null) {
            model.addAttribute("profile", profileMapper.toDto(profile));
        }

        model.addAttribute("isTeacher", profile.getUserId().isTeacher());
        return "myProfile";
    }

    @PostMapping("/edit/photo")
    public String editPhoto(@RequestParam MultipartFile photo, Model model) {
        Map<String, String> result = profileService.changePhoto(photo);
        Profile profile = profileService.getCurrentProfile();

        model.mergeAttributes(result);
        model.addAttribute("profile", profileMapper.toDto(profile));
        model.addAttribute("isTeacher", profile.getUserId().isTeacher());

        return "editMyProfile";
    }

    @PostMapping("/edit")
    public String editProfile(EditProfileDto editProfileDto, Model model) {

        Map<String, String> result = profileService.editProfile(editProfileDto);

        if(result.containsKey(ProfileService.SUCCESS_STATUS)) {
            return "redirect:/profile";
        }

        model.mergeAttributes(result);

        Profile profile = profileService.getCurrentProfile();

        if(profile != null) {
            ProfileDto profileDto = profileMapper.toDto(profile);
            profileDto.setFirstName(editProfileDto.getFirstName());
            profileDto.setLastName(editProfileDto.getLastName());
            model.addAttribute("profile", profileDto);
            model.addAttribute("isTeacher", profile.getUserId().isTeacher());
        }

        return "editMyProfile";
    }

    @GetMapping("/edit")
    public String editProfilePage(Model model) {
        Profile profile = profileService.getCurrentProfile();

        if(profile != null) {
            model.addAttribute("profile", profileMapper.toDto(profile));
            model.addAttribute("isTeacher", profile.getUserId().isTeacher());
        }

        return "editMyProfile";
    }

    @GetMapping("/view/{id}/**")
    public String viewProfile(@PathVariable Long id, Optional<Integer> page,
                              Model model, HttpServletRequest request) {
        Profile userToViewProfile = profileService.getProfileById(id);
        ProfileDto profileDto = profileMapper.toDto(userToViewProfile);
        model.addAttribute("user_id", userToViewProfile.getUserId().getId());
        if(userToViewProfile.getUserId().isTeacher()) {
            TeacherProfileDto teacherProfileDto = new TeacherProfileDto(profileDto);
            teacherProfileDto.setStudentsGroups(profileService.getGroupsByTeacher(userToViewProfile).stream()
                                                                .map(Group::getName)
                                                                .collect(Collectors.toList()));
            teacherProfileDto.setSubjects(profileService.getSubjectsByTeacher(userToViewProfile).stream()
                                                                .map(Subject::getName)
                                                                .collect(Collectors.toList()));
            model.addAttribute("profileData", teacherProfileDto);
            try {
                String storagePath = PathUtils.extractStoragePath(VIEW_PROFILE_MAPPING_PATTERN, request.getRequestURI());
                String requestMappingPath = "/profile/view/" + id;
                List<StorageNode> nodes = storageService.getPublicNodes(id, page.orElse(0), storagePath);
                nodes.sort(new NodesComparator());
                model.addAttribute("nodes", nodes.stream()
                        .map(storageNodeDtoMapper::toDto)
                        .collect(Collectors.toList()));
                String fullPath = storagePath != null ? requestMappingPath + "/" + storagePath : requestMappingPath;
                model.addAttribute("currentUrl", fullPath);
                model.addAttribute("root", requestMappingPath);
                model.addAttribute("currentDirectory", PathUtils.extractCurrentDirectory(fullPath));
                model.addAttribute("path", PathUtils.extractNavigationInfo(storagePath, requestMappingPath));
            } catch (BaseException e) {
                model.addAttribute("error", e.getMessage());
            }
        } else {
            model.addAttribute("profileData", profileDto);
        }
        return "viewProfilePage";
    }

    @GetMapping("/teachers")
	public String getMyTeachers(Model model) {
    	List<Profile> myTeachers = profileService.getMyTeachers();
    	if(!myTeachers.isEmpty()) {
		    fetchProfileListPhotos(myTeachers);
		    model.addAttribute("teachers", myTeachers);
	    }
		return "listOfTeachers";
    }

    private void fetchProfileListPhotos(List<Profile> profiles) {
    	profiles.forEach(this::fetchProfilePhoto);
    }

    private void fetchProfilePhoto(Profile profile) {
    	String photo = filesService.loadPhoto(profile.getPhotoUrl());
    	profile.setPhotoUrl(photo);
    }

}
