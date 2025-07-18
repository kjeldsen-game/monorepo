package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.exceptions.BadRequestException;
import com.kjeldsen.auth.domain.exceptions.UnauthorizedException;
import com.kjeldsen.auth.domain.repositories.UserWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChangePasswordUseCase {

    private final GetUserUseCase getUserUseCase;
    private final UserWriteRepository userWriteRepository;
    private final PasswordEncoder passwordEncoder;

    public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
        User user = getUserUseCase.getCurrent();
        log.info("Changing password for user {}", user.getId());

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UnauthorizedException("Old passwords does not match!");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new BadRequestException("New passwords do not match!");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userWriteRepository.save(user);
    }
}
