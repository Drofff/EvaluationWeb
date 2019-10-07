package com.edu.EvaluationWeb.service;

import com.edu.EvaluationWeb.component.UserContext;
import com.edu.EvaluationWeb.entity.Group;
import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.entity.User;
import com.edu.EvaluationWeb.exception.BaseException;
import com.edu.EvaluationWeb.repository.GroupRepository;
import com.edu.EvaluationWeb.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final ProfileRepository profileRepository;
    private final UserContext userContext;
    private final GroupRepository groupRepository;

    private static final Integer PAGE_SIZE = 20;

    @Autowired
    public StudentService(ProfileRepository profileRepository, UserContext userContext,
                          GroupRepository groupRepository) {
        this.profileRepository = profileRepository;
        this.userContext = userContext;
        this.groupRepository = groupRepository;
    }

    public Page<Profile> getMyStudents(Integer page) {
        User currentUser = userContext.getCurrentUser();
        validateIsTeacher(currentUser);
        return profileRepository.findByTeacher(currentUser.getProfile(), PageRequest.of(page, PAGE_SIZE));
    }

    private void validateIsTeacher(User user) {
        if(!user.isTeacher()) {
            throw new BaseException("Provided user is not teacher");
        }
    }

    public Page<Profile> findByNameAndGroupIfPresent(Optional<String> name, List<Long> groupIds, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Set<Group> groups = loadGroups(groupIds);
        if(name.isPresent() && !groupIds.isEmpty()) {
            return profileRepository.findByNameAndInGroups(name.get(), groups, pageRequest);
        }
        if(name.isPresent()) {
            return profileRepository.findByName(name.get(), pageRequest);
        }
        if(!groupIds.isEmpty()) {
            return profileRepository.findByInGroups(groups, pageRequest);
        }
        return getMyStudents(page);
    }

    private Set<Group> loadGroups(List<Long> groupIds) {
        return groupIds.stream()
                .map(groupId -> groupRepository.findById(groupId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Set<Group> getMyGroups() {
        User user = userContext.getCurrentUser();
        return groupRepository.findByTeacher(user.getProfile());
    }

}
