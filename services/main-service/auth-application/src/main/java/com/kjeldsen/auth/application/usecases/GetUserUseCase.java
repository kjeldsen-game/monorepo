package com.kjeldsen.auth.application.usecases;


import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.exceptions.UserNotFoundException;
import com.kjeldsen.auth.domain.exceptions.UserNotLoggedException;
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
        log.info("GetUserUseCase method getCurrent with id {}" , SecurityUtils.getCurrentUserId());
        if (SecurityUtils.getCurrentUserId() == null) {
            throw new UserNotLoggedException();
        } else {
            Optional<User> optUser = userReadRepository.findByUserId(SecurityUtils.getCurrentUserId());
            if (optUser.isPresent()) {
                return optUser.get();
            } else {
                throw new UserNotLoggedException();
            }
        }
    }

    public User getUserByEmail(String email) {
        log.info("GetUserUseCase method getUserByEmail with email {}", email);
        Optional<User> optUser = userReadRepository.findByEmail(email);
        if (optUser.isPresent()) {
            return optUser.get();
        } else {
            throw new UserNotFoundException();
        }
    }
}
