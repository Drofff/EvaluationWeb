package com.edu.EvaluationWeb.component;

import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.entity.User;
import com.edu.EvaluationWeb.repository.UserRepository;
import com.edu.EvaluationWeb.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Objects;

@Component
public class UserContext {

    private final UserRepository userRepository;

    @Autowired
    public UserContext(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.isNull(authentication) || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(User.class.isAssignableFrom(principal.getClass())) {
            return (User) principal;
        }
        return userRepository.findByUsername(authentication.getName());
    }

    public boolean isCurrentUser(User user) {
        return getCurrentUser().getId().equals(user.getId());
    }

}
