package com.kjeldsen.auth.usecases;

import com.kjeldsen.auth.domain.events.SignUpEvent;
import com.kjeldsen.auth.persistence.repositories.write.SignUpEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpUseCase {

    private final SignUpEventRepository signUpEventRepository;

    public void signUp(SignUpEvent signUpEvent) {
        // TODO validate
        signUpEventRepository.save(signUpEvent);
    }
}
