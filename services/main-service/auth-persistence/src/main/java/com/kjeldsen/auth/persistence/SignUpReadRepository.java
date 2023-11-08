package com.kjeldsen.auth.persistence;

import com.kjeldsen.auth.domain.SignUp;
import com.kjeldsen.auth.mongo.repositories.SignUpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SignUpReadRepository {

    private final SignUpRepository signUpRepository;

    public Optional<SignUp> findByUsernameIgnoreCase(String id) {
        return signUpRepository.findByUsernameIgnoreCase(id);
    }

}
