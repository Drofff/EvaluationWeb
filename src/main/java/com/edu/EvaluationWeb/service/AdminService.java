package com.edu.EvaluationWeb.service;

import com.edu.EvaluationWeb.dto.GroupOptionDto;
import com.edu.EvaluationWeb.entity.Group;
import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.entity.Role;
import com.edu.EvaluationWeb.entity.User;
import com.edu.EvaluationWeb.exception.BaseException;
import com.edu.EvaluationWeb.repository.GroupRepository;
import com.edu.EvaluationWeb.repository.ProfileRepository;
import com.edu.EvaluationWeb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final ProfileRepository profileRepository;
    private final TestService testService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final FilesService filesService;

    @Value("${mail.message.account.created}")
    private String accountCreatedMailMessage;

    private static final String USERNAME_MARKER = "USERNAME";
    private static final String PASSWORD_MARKER = "PASSWORD";
    private static final Integer PAGE_SIZE = 10;

    @Autowired
    public AdminService(ProfileRepository profileRepository, TestService testService,
                        UserRepository userRepository, GroupRepository groupRepository,
                        PasswordEncoder passwordEncoder, MailService mailService,
                        FilesService filesService) {
        this.profileRepository = profileRepository;
        this.testService = testService;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.filesService = filesService;
    }

    public void addGroup(String name) {
        if(Objects.isNull(name) || name.isEmpty()) {
            throw new BaseException("Invalid group name");
        }
        if(groupRepository.findByName(name).isPresent()) {
            throw new BaseException("Group with such name already exists");
        }
        Group group = new Group();
        group.setName(name);
        groupRepository.save(group);
    }

    public void loadGroupPhotos(Group group) {
        group.getMembers().forEach(this::loadProfilePhoto);
        group.getTeachers().forEach(this::loadProfilePhoto);
    }

    private void loadProfilePhoto(Profile profile) {
        String photoUrl = profile.getPhotoUrl();
        if(Objects.nonNull(photoUrl) && !photoUrl.startsWith(FilesService.IMAGE_PREFIX)) {
            String photo = filesService.loadPhoto(photoUrl);
            profile.setPhotoUrl(photo);
        }
    }

    public Group getGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new BaseException("Group with such id do not exists"));
    }

    public void removeTeacherFromGroup(Long teacherId, Long groupId) {
        Group group = getGroupById(groupId);
        Profile teacher = getTeacherFromGroupById(teacherId, group)
                .orElseThrow(() -> new BaseException("No such teacher in group " + group.getName()));;
        group.getTeachers().remove(teacher);
        groupRepository.save(group);
    }

    public void addTeacherToGroup(Long teacherId, Long groupId) {
        Group group = getGroupById(groupId);
        if(getTeacherFromGroupById(teacherId, group).isPresent()) {
            throw new BaseException("User is already teacher in this group");
        }
        Profile teacher = profileRepository.findById(teacherId)
                .filter(profile -> profile.getUserId().isTeacher())
                .orElseThrow(() -> new BaseException("Teacher with such id do not exists"));
        group.getTeachers().add(teacher);
        groupRepository.save(group);
    }

    public void addMemberToGroup(Long memberId, Long groupId) {
        Group group = getGroupById(groupId);
        Profile member = profileRepository.findById(memberId)
                .orElseThrow(() -> new BaseException("User with such id do not exists"));
        member.setGroup(group);
        profileRepository.save(member);
    }

    private Optional<Profile> getTeacherFromGroupById(Long teacherId, Group group) {
        return group.getTeachers()
                .stream()
                .filter(t -> t.getId().equals(teacherId))
                .findFirst();
    }

    public List<Profile> getAllTeachers() {
        return profileRepository.findAllTeachers();
    }

    public List<Profile> getAllUsers() {
        return profileRepository.findAll();
    }

    public Page<Group> getAllGroups(Optional<String> name, Integer page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return name.isPresent() ? groupRepository.findByNameMatch(name.get(), pageable) :
                groupRepository.findAll(pageable);
    }

    public List<GroupOptionDto> getAllGroups(Set<String> selected) {
        List<GroupOptionDto> groups = groupRepository.findAll().stream()
                .map(GroupOptionDto::new)
                .collect(Collectors.toList());
        if(Objects.nonNull(selected) && !selected.isEmpty()) {
            groups.forEach(group -> group.setSelected(selected.contains(group.getName())));
        }
        return groups;
    }

    public Page<Profile> getAllUsers(Optional<String> name,
                                     Optional<Set<String>> groups, Integer page) {
        Set<Group> groupsObjects = groups.map(testService::parseGroupsByNames)
                .orElse(Collections.emptySet());
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        if(name.isPresent() && !groupsObjects.isEmpty()) {
            return profileRepository.findByNameAndInGroups(name.get(), groupsObjects, pageable);
        } else if(name.isPresent()) {
            return profileRepository.findByName(name.get(), pageable);
        } else if(!groupsObjects.isEmpty()) {
            return profileRepository.findByInGroups(groupsObjects, pageable);
        }
        return profileRepository.findAll(pageable);
    }

    public void createAccount(String email, Long groupId, String position, Boolean isTeacher) {
        validateUserExistence(email);
        isTeacher = Optional.ofNullable(isTeacher).orElse(false);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BaseException("Group with such id do not exists"));
        String password = UUID.randomUUID().toString();
        User user = createUser(email, password, isTeacher);
        user = userRepository.save(user);
        Profile profile = createDefaultProfileWithData(position, group, user);
        profile = profileRepository.save(profile);
        updateGroupsMembers(profile, group);
        sendCredentialsToEmail(email, password);
    }

    private void sendCredentialsToEmail(String email, String password) {
        String topic = "Account in EvaluationWeb was successfully created";
        String text = accountCreatedMailMessage.replace(USERNAME_MARKER, email)
                .replace(PASSWORD_MARKER, password);
        mailService.sendEmail(topic, text, email);
    }

    private void validateUserExistence(String email) {
        User user = userRepository.findByUsername(email);
        if(Objects.nonNull(user)) {
            throw new BaseException("User with such email already exists");
        }
    }

    private void updateGroupsMembers(Profile profile, Group group) {
        List<Profile> members = group.getMembers();
        members.add(profile);
        group.setMembers(members);
        groupRepository.save(group);
    }

    private User createUser(String username, String password, Boolean isTeacher) {
        User user = new User();
        user.setUsername(username);
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        Set<Role> roles = new HashSet<>();
        if(isTeacher) {
            roles.add(Role.TEACHER);
        } else {
            roles.add(Role.USER);
        }
        user.setRoles(roles);
        return user;
    }

    private Profile createDefaultProfileWithData(String position, Group group, User user) {
        Profile profile = new Profile();
        profile.setPosition(position);
        profile.setUserId(user);
        profile.setGroup(group);
        return profile;
    }


}
