package com.drofff.edu.component;

import com.drofff.edu.entity.Profile;
import com.drofff.edu.entity.User;
import com.drofff.edu.service.FilesService;
import com.drofff.edu.utils.ParseUtils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class ProfileDataInterceptor extends HandlerInterceptorAdapter {

    private final UserContext userContext;
    private final FilesService filesService;

    private static final String REGISTRATION_URI = "/registration";
    private static final String REDIRECT_PATTERN = "(redirect:).*";

    public ProfileDataInterceptor(UserContext userContext, FilesService filesService) {
        this.userContext = userContext;
        this.filesService = filesService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User currentUser = userContext.getCurrentUser();
        if(needRegistrationRedirect(currentUser, request.getRequestURI())) {
            response.sendRedirect(request.getContextPath() + REGISTRATION_URI);
            return false;
        }
        return true;
    }

    private boolean needRegistrationRedirect(User user, String requestUri) {
        return Objects.nonNull(user) && user.isNew() && !requestUri.equals(REGISTRATION_URI);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
        if(Objects.isNull(modelAndView)) {
            return;
        }
        User currentUser = userContext.getCurrentUser();
        if(isAuthorizedAndNotNew(currentUser) && !isRedirect(modelAndView.getViewName())) {
            addUserProfileDataToModel(currentUser, modelAndView);
        }
    }

    private boolean isAuthorizedAndNotNew(User user) {
        return Objects.nonNull(user) && !user.isNew();
    }

    private boolean isRedirect(String viewName) {
        return Objects.nonNull(viewName) && ParseUtils.matches(viewName, REDIRECT_PATTERN);
    }

    private void addUserProfileDataToModel(User user, ModelAndView modelAndView) {
        Profile profile = user.getProfile();
        Boolean isTeacher = user.isTeacher();
        Boolean isAdmin = user.isAdmin();
        String name = profile.getFirstName() + " " + profile.getLastName();
        String photoUrl = filesService.loadPhoto(profile.getPhotoUrl());
        modelAndView.addObject("isTeacher", isTeacher);
        modelAndView.addObject("isAdmin", isAdmin);
        modelAndView.addObject("name", name);
        modelAndView.addObject("photoUrl", photoUrl);
    }

}