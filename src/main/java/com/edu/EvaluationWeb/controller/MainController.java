package com.edu.EvaluationWeb.controller;

import com.edu.EvaluationWeb.repository.UserRepository;
import com.edu.EvaluationWeb.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@Controller
public class MainController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CalendarService calendarService;

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


}
