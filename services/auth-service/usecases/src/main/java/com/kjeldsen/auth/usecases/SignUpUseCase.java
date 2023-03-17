package com.kjeldsen.auth.usecases;

import com.kjeldsen.auth.domain.SignUp;
import com.kjeldsen.auth.kafka.producers.AuthSignUpProducer;
import com.kjeldsen.auth.persistence.SignUpReadRepository;
import com.kjeldsen.auth.persistence.SignUpWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SignUpUseCase {

    private final SignUpWriteRepository signUpWriteRepository;
    private final SignUpReadRepository signUpReadRepository;
    private final AuthSignUpProducer authSignUpProducer;

    // TODO Miguel this should be transactional. Add integration test that makes sure of the rollback in db if kafka message fails
    public void signUp(SignUp signUp) throws ResponseStatusException {
        throwIfFound(signUp);
        signUpWriteRepository.save(signUp);
        authSignUpProducer.send(signUp);
    }

    private void throwIfFound(SignUp signUp) {
        signUpReadRepository.findByUsernameIgnoreCase(signUp.getUsername())
            .ifPresent(conflictingSignUp -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Username %s already in use", conflictingSignUp.getUsername()));
            });
    }
}
