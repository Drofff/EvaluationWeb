package com.drofff.edu.service;

import com.drofff.edu.cache.PasswordCache;
import com.drofff.edu.cache.TokenCache;
import com.drofff.edu.entity.Profile;
import com.drofff.edu.entity.User;
import com.drofff.edu.repository.UserRepository;
import com.drofff.edu.utils.MailUtils;
import com.drofff.edu.component.UserContext;
import com.drofff.edu.exception.BaseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class PasswordService {

    private final TokenCache tokenCache;
    private final PasswordCache passwordCache;
    private final UserContext userContext;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private static final String LINK_MARKER = "LINK";
    private static final String NAME_MARKER = "NAME";

    private static final String RECOVERY_TOPIC = "Password recovery";

    @Value("${data.url}")
    private String serverUrl;

    @Value("${mail.message.password.change}")
    private String passwordChangeRequestMessage;

    @Value("${mail.message.password.recover}")
    private String passwordRecoveryRequestMessage;

    @Autowired
    public PasswordService(TokenCache tokenCache, PasswordCache passwordCache,
                           UserContext userContext, MailService mailService,
                           PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.tokenCache = tokenCache;
        this.passwordCache = passwordCache;
        this.userContext = userContext;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void requestChange(String password) {
        if(password.isEmpty() || password.length() < 6) {
            throw new SecurityException("Invalid password. Minimum length is 6 symbols");
        }
        User currentUser = userContext.getCurrentUser();
        Profile profile = currentUser.getProfile();
        passwordCache.save(currentUser.getUsername(), passwordEncoder.encode(password));
        String token = UUID.randomUUID().toString();
        tokenCache.save(currentUser.getUsername(), token);
        mailService.sendEmail("Password change request",
                generateTokenMailText(profile.getFirstName() + " " + profile.getLastName(), token),
                        currentUser.getUsername());
    }

    private String generateTokenMailText(String name, String token) {
        Map<String, String> map = new HashMap<>();
        map.put(NAME_MARKER, name);
        String link = UriComponentsBuilder.fromUriString(serverUrl)
                .pathSegment("password", "change", "confirm")
                .queryParam("token", token)
                .build()
                .toString();
        map.put(LINK_MARKER, link);
        return MailUtils.fillMarkers(map.entrySet().iterator(), passwordChangeRequestMessage);
    }

    public void confirmChange(String token) {
        User currentUser = userContext.getCurrentUser();
        String originalToken = tokenCache.load(currentUser.getUsername());
        if(!originalToken.equals(token)) {
            throw new SecurityException("Invalid confirmation token");
        }
        String password = passwordCache.load(currentUser.getUsername());
        tokenCache.remove(currentUser.getUsername());
        passwordCache.remove(currentUser.getUsername());
        currentUser.setPassword(password);
        userRepository.save(currentUser);
    }

    public void validateToken(String email, String token) {
        String originalToken = tokenCache.load(email);
        if(!originalToken.equals(token)) {
            throw new BaseException("Invalid token");
        }
    }

    public Authentication authenticateUserByEmail(String email) {
        return getUserByUsername(email).getAuthenticationToken();
    }

    private User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(Objects.isNull(user)) {
            throw new BaseException("User with such username do not exists");
        }
        return user;
    }

    public void requestPasswordRecovery(String email) {
        User recoveryUser = validateEmailAndFindUser(email);
        String token = createRecoveryToken(email);
        sendRecoveryMail(recoveryUser, token);
    }

    private String createRecoveryToken(String email) {
        String token = UUID.randomUUID().toString();
        tokenCache.save(email, token);
        return token;
    }

    private User validateEmailAndFindUser(String email) {
        if(Objects.isNull(email) || email.isEmpty()) {
            throw new BaseException("Please, provide valid email address");
        }
        User user = userRepository.findByUsername(email);
        if(Objects.isNull(user)) {
            throw new BaseException("User with such email do not exists");
        }
        return user;
    }

    private void sendRecoveryMail(User recoveryUser, String token) {
        Profile currentUserProfile = recoveryUser.getProfile();
        String link = UriComponentsBuilder.fromUriString(serverUrl)
                .pathSegment("password", "recover", recoveryUser.getUsername(), "token", token)
                .build()
                .toString();
        String message = passwordRecoveryRequestMessage
                .replace(NAME_MARKER, currentUserProfile.getFirstName() +
                        " " + currentUserProfile.getLastName())
                .replace(LINK_MARKER, link);
        mailService.sendEmail(RECOVERY_TOPIC, message, recoveryUser.getUsername());
    }

    public void recoveryChangePassword(String token, String password, String repeatedPassword) {
        User currentUser = userContext.getCurrentUser();
        validateToken(currentUser.getUsername(), token);
        validatePassword(password, repeatedPassword);
        tokenCache.remove(currentUser.getUsername());
        encodeAndSetPassword(currentUser, password);
        userRepository.save(currentUser);
    }

    private void encodeAndSetPassword(User user, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
    }

    private void validatePassword(String password, String repeatedPassword) {
        if(Objects.isNull(password) || Objects.isNull(repeatedPassword)) {
            throw new BaseException("Provided password or repeated password is null");
        }
        if(password.isEmpty() || password.length() < 6) {
            throw new BaseException("Minimal password length is 6 symbols");
        }
        if(!password.equals(repeatedPassword)) {
            throw new BaseException("Repeated password should match password");
        }
    }

}
