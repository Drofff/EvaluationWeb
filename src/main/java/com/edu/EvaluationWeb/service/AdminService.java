package com.edu.EvaluationWeb.service;

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

@Service
public class AdminService {

    private final ProfileRepository profileRepository;
    private final TestService testService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Value("${mail.message.account.created}")
    private String accountCreatedMailMessage;

    private static final String USERNAME_MARKER = "USERNAME";
    private static final String PASSWORD_MARKER = "PASSWORD";
    private static final Integer PAGE_SIZE = 10;

    @Autowired
    public AdminService(ProfileRepository profileRepository, TestService testService,
                        UserRepository userRepository, GroupRepository groupRepository,
                        PasswordEncoder passwordEncoder, MailService mailService) {
        this.profileRepository = profileRepository;
        this.testService = testService;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
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

    public Page<Group> getAllGroups(Optional<String> name, Integer page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return name.isPresent() ? groupRepository.findByNameMatch(name.get()) :
                groupRepository.findAll(pageable);
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
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BaseException("Group with such id do not exists"));
        String password = UUID.randomUUID().toString();
        User user = createUser(email, password, isTeacher);
        user = userRepository.save(user);
        Profile profile = createDefaultProfileWithData(position, group, user);
        profile = profileRepository.save(profile);
        updateGroup(profile, group, isTeacher);
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

    private void updateGroup(Profile profile, Group group, Boolean isTeacher) {
        if(isTeacher) {
            updateGroupTeachers(profile, group);
        } else {
            updateGroupsMembers(profile, group);
        }
    }

    private void updateGroupTeachers(Profile profile, Group group) {
        List<Profile> teachers = group.getTeachers();
        teachers.add(profile);
        group.setTeachers(teachers);
        groupRepository.save(group);
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

    public void changeGroupForUser(Long userId, Long groupId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException("User with such id do not exists"));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BaseException("Group with such id do not exists"));
        Profile profile = user.getProfile();
        removeFromGroup(profile.getId(), profile.getGroup(), profile.getUserId().isTeacher());
        updateGroup(profile, group, profile.getUserId().isTeacher());
    }

    private void removeFromGroup(Long profileId, Group group, Boolean isTeacher) {
        if(isTeacher) {
            removeTeacherFromGroup(profileId, group);
        } else {
            removeMemberFromGroup(profileId, group);
        }
    }

    private void removeTeacherFromGroup(Long teacherId, Group group) {
        List<Profile> teachers = group.getTeachers();
        Profile teacher = teachers.stream()
                .filter(x -> x.getId().equals(teacherId))
                .findFirst().orElse(null);
        if(Objects.nonNull(teacher)) {
            teachers.remove(teacher);
            group.setTeachers(teachers);
            groupRepository.save(group);
        }
    }

    private void removeMemberFromGroup(Long memberId, Group group) {
        List<Profile> members = group.getMembers();
        Profile member = members.stream()
                .filter(x -> x.getId().equals(memberId))
                .findFirst().orElse(null);
        if(Objects.nonNull(member)) {
            members.remove(member);
            group.setMembers(members);
            groupRepository.save(group);
        }
    }

}
