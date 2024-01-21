package com.kjeldsen.auth;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    /*
     * Provides authentication functionality for other services. Ideally this remains the only class
     * that is imported in other modules, and we just expose a few helper methods from here.
     */

    private final UserRepository userRepository;

    public Optional<User> currentUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null || subject.getPrincipal() == null) {
            return Optional.empty();
        }
        String email = subject.getPrincipal().toString();
        return userRepository.findByEmail(email);
    }

    public Optional<String> currentUserId() {
        return currentUser().map(User::getId);
    }
}
