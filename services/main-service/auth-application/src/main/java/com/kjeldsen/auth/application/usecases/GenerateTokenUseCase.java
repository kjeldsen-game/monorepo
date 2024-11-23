package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.providers.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        com.kjeldsen.auth.domain.User user = getUserUseCase.getUserByEmail(email);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return jwtTokenProvider.generateToken(user.getId(), user.getRoles());
    }
}
