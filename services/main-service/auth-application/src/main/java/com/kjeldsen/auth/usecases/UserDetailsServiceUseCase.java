package com.kjeldsen.auth.usecases;

import com.kjeldsen.auth.domain.SignUp;
import com.kjeldsen.auth.persistence.SignUpReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceUseCase /*implements UserDetailsService */ {

    private final SignUpReadRepository signUpReadRepository;

    public SignUp loadUserByUsername(String username) {
        return signUpReadRepository.findByUsernameIgnoreCase(username)
            .orElseThrow(() -> new RuntimeException(String.format("Username %s not found", username)));
    }
}
