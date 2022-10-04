package com.kjeldsen.auth.persistence;

import com.kjeldsen.auth.domain.SignUp;
import com.kjeldsen.auth.mongo.repositories.SignUpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpWriteRepository {

    private final SignUpRepository signUpRepository;

    public SignUp save(SignUp signUp) {
        return signUpRepository.save(signUp);
    }

}
