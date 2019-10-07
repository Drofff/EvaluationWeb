package com.edu.EvaluationWeb.component;

import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.entity.User;
import com.edu.EvaluationWeb.service.FilesService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class ProfileDataInterceptor extends HandlerInterceptorAdapter {

    private final UserContext userContext;
    private final FilesService filesService;

    public ProfileDataInterceptor(UserContext userContext, FilesService filesService) {
        this.userContext = userContext;
        this.filesService = filesService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
        User currentUser = userContext.getCurrentUser();
        if(Objects.isNull(currentUser) || Objects.isNull(modelAndView)) {
            return;
        }
        Profile profile = currentUser.getProfile();
        modelAndView.addObject("isTeacher", currentUser.isTeacher());
        modelAndView.addObject("name", profile.getFirstName() + " " + profile.getLastName());
        modelAndView.addObject("photoUrl", filesService.loadPhoto(profile.getPhotoUrl()));
    }

}
