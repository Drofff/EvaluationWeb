package com.edu.EvaluationWeb.controller;

import com.edu.EvaluationWeb.exception.BaseException;
import com.edu.EvaluationWeb.exception.CacheException;
import com.edu.EvaluationWeb.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/password")
public class PasswordController {

    private final PasswordService passwordService;

    @Autowired
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @GetMapping("/change")
    public String changePasswordPage() {
        return "changePasswordPage";
    }

    @PostMapping("/change")
    public String changePassword(String password, String repeatedPassword, Model model) {
        if(password.equals(repeatedPassword)) {
            try {
                passwordService.requestChange(password);
            } catch(SecurityException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "changePasswordPage";
            }
        } else {
            model.addAttribute("errorMessage", "Invalid input. Passwords have to match");
            return "changePasswordPage";
        }
        model.addAttribute("message", "We ask you to, please, check your email inbox to confirm password change");
        return "checkYourMail";
    }

    @GetMapping("/change/confirm")
    public String confirmPasswordChange(String token, Model model) {
        try {
            passwordService.confirmChange(token);
            model.addAttribute("message", "Password was successfully changed");
        } catch(SecurityException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch(CacheException e) {
            model.addAttribute("errorMessage", "Provided confirmation token has already expired");
        }
        return "changePasswordPage";
    }

    @GetMapping("/recover")
    public String passwordRecoveryPage() {
        return "recoverPasswordPage";
    }

    @PostMapping("/recover")
    public String requestRecovery(String email, Model model) {
        try {
            passwordService.requestPasswordRecovery(email);
            model.addAttribute("message", "Password recovery were successfully accepted." +
                    " Please check you email inbox to continue recovery process");
        } catch(BaseException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("invalidEmail", true);
            return "recoverPasswordPage";
        }
        return "checkYourMail";
    }

    @GetMapping("/recover/{email}/token/{token}")
    public String recoverByToken(@PathVariable String token, @PathVariable String email,
                                 Model model, HttpServletRequest request) {
        try {
            passwordService.validateToken(email, token);
            Authentication authentication = passwordService.authenticateUserByEmail(email);
            putAuthenticationIntoSession(authentication, request.getSession());
        } catch(CacheException | BaseException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "recoverPasswordPage";
        }
        model.addAttribute("token", token);
        return "recoveryChangePasswordPage";
    }

    private void putAuthenticationIntoSession(Authentication authentication, HttpSession session) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }

    @PostMapping("/recover/change")
    public String recoveryChangePassword(String token, String password,
                                         String repeatedPassword, Model model) {
        try {
            passwordService.recoveryChangePassword(token, password, repeatedPassword);
        } catch(CacheException | BaseException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("token", token);
            return "recoveryChangePasswordPage";
        }
        return "redirect:/";
    }

}
