package com.edu.EvaluationWeb.service;

import com.edu.EvaluationWeb.component.UserContext;
import com.edu.EvaluationWeb.constants.ProfileConstants;
import com.edu.EvaluationWeb.dto.EditProfileDto;
import com.edu.EvaluationWeb.entity.Group;
import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.entity.Subject;
import com.edu.EvaluationWeb.entity.User;
import com.edu.EvaluationWeb.enums.FileType;
import com.edu.EvaluationWeb.exception.BaseException;
import com.edu.EvaluationWeb.repository.GroupRepository;
import com.edu.EvaluationWeb.repository.ProfileRepository;
import com.edu.EvaluationWeb.repository.SubjectRepository;
import com.edu.EvaluationWeb.utils.ValidationUtils;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ProfileService {

    private final UserContext userContext;
    private final ValidationService validationService;
    private final ProfileRepository profileRepository;
    private final FilesService filesService;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;

    public static final String SUCCESS_STATUS = "SUCCESS";

    @Autowired
    public ProfileService(UserContext userContext, ValidationService validationService,
                          ProfileRepository profileRepository, FilesService filesService,
                          GroupRepository groupRepository, SubjectRepository subjectRepository) {
        this.userContext = userContext;
        this.validationService = validationService;
        this.profileRepository = profileRepository;
        this.filesService = filesService;
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;
    }

    public Map<String, String> editProfile(EditProfileDto profileDto) {
        if(profileDto == null) {
            return Collections.singletonMap("Error", "Form can not be empty");
        }
        Map<String, String> validationResult = validationService.validate(profileDto);
        if(validationResult.size() > 0) {
            return validationResult;
        }
        Profile profile = userContext.getCurrentUser().getProfile();
        profile.setFirstName(profileDto.getFirstName());
        profile.setLastName(profileDto.getLastName());
        profileRepository.save(profile);
        return Collections.singletonMap(SUCCESS_STATUS, "Successfully saved profile data");
    }

    public Profile getCurrentProfile() {
        return userContext.getCurrentUser().getProfile();
    }

    public Map<String, String> changePhoto(MultipartFile file) {
        User currentUser = userContext.getCurrentUser();
        Profile profile = currentUser.getProfile();

        if(file != null) {
            String photoPath = filesService.save(file, FileType.PHOTO, currentUser);
            if(photoPath == null) {
                return Collections.singletonMap("PhotoError", "Error while uploading this photo");
            }
            profile.setPhotoUrl(photoPath);
            profileRepository.save(profile);
        } else {
            return Collections.singletonMap("PhotoError", "Please, provide photo");
        }

        return Collections.singletonMap(SUCCESS_STATUS, "Successfully saved");
    }

    public Set<Group> getMyGroups() {
        return groupRepository.findByTeacher(userContext.getCurrentUser().getProfile());
    }

    public Profile getProfileById(Long id) {
        Optional<Profile> profileOptional = profileRepository.findById(id);
        if(!profileOptional.isPresent()) {
            throw new BaseException("Profile with such id do not exists");
        }
        return profileOptional.get();
    }

    public Set<Group> getGroupsByTeacher(Profile teacher) {
        return groupRepository.findByTeacher(teacher);
    }

    public List<Subject> getSubjectsByTeacher(Profile teacher) {
        return subjectRepository.findByTeacher(teacher.getUserId());
    }

    public void registerProfile(String firstName, String lastName, MultipartFile photo) {
        User user = userContext.getCurrentUser();
        if(!user.isNew()) {
            throw new BaseException("User is already registered");
        }
        validateProfileData(firstName, lastName, photo);
        Profile profile = user.getProfile();
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        String photoUrl = savePhoto(photo, user);
        profile.setPhotoUrl(photoUrl);
        profileRepository.save(profile);
    }

    private String savePhoto(MultipartFile photo, User user) {
        return filesService.save(photo, FileType.PHOTO, user);
    }

    private void validateProfileData(String firstName, String lastName, MultipartFile photo) {
        validateName(firstName);
        validateName(lastName);
        validatePhoto(photo);
    }

    private void validatePhoto(MultipartFile photo) {
        ValidationUtils.validateNonNull(photo, "Please provide your photo");
        ValidationUtils.validateFileSize(photo);
    }

    private void validateName(String name) {
        if(Objects.isNull(name) || name.isEmpty() || !name.matches(ProfileConstants.NAME_REGEX)) {
            throw new BaseException("Sorry but we accept only non-empty names without special symbols, spaces and digits");
        }
    }

    public List<Profile> getMyTeachers() {
    	User currentUser = userContext.getCurrentUser();
    	Profile profile = currentUser.getProfile();
    	return profileRepository.findAllTeachersByGroupId(profile.getGroup().getId());
    }

}