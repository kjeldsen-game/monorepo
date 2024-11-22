package com.kjeldsen.auth.application.usecases;


import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.repositories.UserReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetUserUseCase {

    private final UserReadRepository userReadRepository;

    public User getCurrent() {
        Optional<User> optUser = userReadRepository.findByUserId(Objects.requireNonNull(SecurityUtils.getCurrentUserId()));
        if (optUser.isPresent()) {
            return optUser.get();
        } else {
            throw new RuntimeException("User not logged in");
        }
    }

    public User getUserByEmail(String email) {
        Optional<User> optUser = userReadRepository.findByEmail(email);
        if (optUser.isPresent()) {
            return optUser.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
