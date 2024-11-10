package com.kjeldsen.auth;

import com.kjeldsen.auth.authorization.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public Optional<User> currentUser() {
        return userRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId()));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
