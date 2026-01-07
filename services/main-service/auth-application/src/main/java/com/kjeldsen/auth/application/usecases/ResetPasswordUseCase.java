package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.PasswordResetToken;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.exceptions.UnauthorizedException;
import com.kjeldsen.auth.domain.repositories.PasswordResetTokenReadRepository;
import com.kjeldsen.auth.domain.repositories.PasswordResetTokenWriteRepository;
import com.kjeldsen.auth.domain.repositories.UserWriteRepository;
import com.kjeldsen.auth.domain.utils.PasswordValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResetPasswordUseCase {

    private final GetUserUseCase getUserUseCase;
    private final PasswordResetTokenReadRepository passwordResetTokenReadRepository;
    private final PasswordResetTokenWriteRepository passwordResetTokenWriteRepository;
    private final UserWriteRepository userWriteRepository;
    private final PasswordEncoder passwordEncoder;


    public void resetPassword(String token, String newPassword, String confirmPassword) {
        log.info("ResetPasswordUseCase for token=******* newPassword=********** {} {}", newPassword, confirmPassword);

        PasswordResetToken passwordResetToken = passwordResetTokenReadRepository.findByToken(token).orElseThrow(
            () -> new UnauthorizedException("Invalid token!"));

        if (Instant.now().isAfter(passwordResetToken.getExpiryDate())) {
            throw new UnauthorizedException("Invalid token!");
        }

        User user = getUserUseCase.getUserById(passwordResetToken.getUserId());
        PasswordValidator.validatePassword(newPassword, confirmPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userWriteRepository.save(user);
        passwordResetTokenWriteRepository.delete(token);
    }
}
