package com.drofff.edu.controller;

import com.drofff.edu.constants.ModelConstants;
import com.drofff.edu.exception.BaseException;
import com.drofff.edu.repository.UserRepository;
import com.drofff.edu.service.CalendarService;
import com.drofff.edu.service.ProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@Controller
public class MainController {

    private final UserRepository userRepository;
    private final CalendarService calendarService;
    private final ProfileService profileService;

    @Autowired
    public MainController(UserRepository userRepository, CalendarService calendarService,
                          ProfileService profileService) {
        this.userRepository = userRepository;
        this.calendarService = calendarService;
        this.profileService = profileService;
    }

    @RequestMapping("/login")
    public String getLoginPage(HttpServletRequest request, Model model) {
        if ( request.getQueryString() != null && request.getQueryString().equals("error") ) {
            model.addAttribute("messageError", "Invalid Credentials");
        }
        return "login";
    }

    @RequestMapping("/")
    public String getMainPage(@RequestParam(required = false) String week,
                              @RequestParam(required = false) String day, Model model) {

        try {

            Integer weekToDisplay = 0;
            Integer dayToDisplay = 1;

            if (week != null) {
                weekToDisplay = Integer.parseInt(week);
            }

            if (day != null) {
                dayToDisplay = Integer.parseInt(day);
            }

            if (day == null && weekToDisplay == 0) {
                dayToDisplay = LocalDate.now().getDayOfWeek().getValue();
            }

            model = calendarService.getMainPageModel(model, dayToDisplay, weekToDisplay);

        } catch (Exception e) {
            e.printStackTrace();
            return "errorPage";
        }

        return "mainPage";
    }

    @GetMapping("/registration")
    public String fillProfileData() {
        return "registrationPage";
    }

    @PostMapping("/registration")
    public String registerUserProfileData(String firstName, String lastName, MultipartFile photo,
                                          Model model) {
        try {
            profileService.registerProfile(firstName, lastName, photo);
            return "redirect:/";
        } catch(BaseException e) {
            model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
        }
        return "registrationPage";
    }

}
