package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.exceptions.BadRequestException;
import com.kjeldsen.auth.domain.exceptions.UnauthorizedException;
import com.kjeldsen.auth.domain.providers.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenerateTokenUseCase {

    private final GetUserUseCase getUserUseCase;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String get(String email, String password) {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new BadRequestException("Invalid email address format!");
        }
        com.kjeldsen.auth.domain.User user;
        try {
            user = getUserUseCase.getUserByEmail(email);
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new UnauthorizedException("Invalid email or password!");
            }
            return jwtTokenProvider.generateToken(user.getId(), user.getRoles());
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid email or password!");
        }
    }
}
