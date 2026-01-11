package com.kjeldsen.auth.application.usecases;


import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.auth.domain.models.User;
import com.kjeldsen.auth.domain.exceptions.NotFoundException;
import com.kjeldsen.auth.domain.exceptions.UnauthorizedException;
import com.kjeldsen.auth.domain.repositories.UserReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetUserUseCase {

    private final UserReadRepository userReadRepository;

    public User getCurrent() {
//        log.debug("GetUserUseCase method getCurrent with id {}" , SecurityUtils.getCurrentUserId());
        if (SecurityUtils.getCurrentUserId() == null) {
            throw new UnauthorizedException("User is not logged in.");
        } else {
            Optional<User> optUser = userReadRepository.findByUserId(SecurityUtils.getCurrentUserId());
            if (optUser.isPresent()) {
                return optUser.get();
            } else {
                throw new UnauthorizedException("User not authorized or not found.");
            }
        }
    }

    public User getUserByEmail(String email) {
//        log.debug("GetUserUseCase method getUserByEmail with email {}", email);
        Optional<User> optUser = userReadRepository.findByEmail(email);
        if (optUser.isPresent()) {
            return optUser.get();
        } else {
            throw new NotFoundException("User with email '" + email + "' not found.");
        }
    }

    public User getUserById(String id) {
//        log.debug("GetUserUseCase method getUserById with id {}", id);
        return userReadRepository.findByUserId(id).orElseThrow(
            () -> new NotFoundException("User not found !"));
    }
}
