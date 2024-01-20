package com.kjeldsen.auth;

import java.util.Optional;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public Optional<String> currentUserId() {
        Subject subject = SecurityUtils.getSubject();

        if (subject == null || subject.getPrincipal() == null) {
            return Optional.empty();
        }
        return Optional.of(subject.getPrincipal().toString());
    }
}
