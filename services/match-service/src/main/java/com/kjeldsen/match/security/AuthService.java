package com.kjeldsen.match.security;

import com.kjeldsen.match.controllers.ValidationException;
import com.kjeldsen.match.models.User;
import com.kjeldsen.match.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public String signup(String email, String password, HttpServletResponse res) {
        validateAuthDetails(email, password);
        User user = User.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .build();
        userRepository.save(user);
        String token = tokenService.generateToken(user);
        storeTokenInCookie(token, res);
        return token;
    }

    public String signin(String email, String password, HttpServletResponse res) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return userRepository.findByEmail(email)
            .map(tokenService::generateToken)
            .map(token -> {
                storeTokenInCookie(token, res);
                return token;
            })
            .orElseThrow(() -> new ValidationException("User not found"));
    }

    private void storeTokenInCookie(String token, HttpServletResponse res) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        // TODO set this in production
        // cookie.setSecure(true);
        // cookie.setHttpOnly(true);
        cookie.setPath("/");
        res.addCookie(cookie);
    }

    private void validateAuthDetails(String email, String password) {
        if (StringUtils.isEmpty(email)) {
            throw new ValidationException("Email cannot be empty");
        }
        if (StringUtils.isEmpty(password)) {
            throw new ValidationException("Password cannot be empty");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ValidationException("Email is already in use");
        }
    }

    public User getUser(Authentication auth) {
        return Optional.ofNullable(auth)
            .map(Authentication::getName)
            .flatMap(userRepository::findByEmail)
            .orElseThrow(() ->
                new BadCredentialsException("You must be signed in to do that."));
    }
}
