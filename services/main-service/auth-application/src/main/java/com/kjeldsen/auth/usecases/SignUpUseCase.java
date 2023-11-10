package com.kjeldsen.auth.usecases;

import com.kjeldsen.auth.domain.SignUp;
import com.kjeldsen.auth.persistence.SignUpReadRepository;
import com.kjeldsen.auth.persistence.SignUpWriteRepository;
import com.kjeldsen.player.application.usecases.CreateTeamUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SignUpUseCase {

    private final SignUpWriteRepository signUpWriteRepository;
    private final SignUpReadRepository signUpReadRepository;
    private final CreateTeamUseCase createTeamUseCase;

    // TODO this should be transactional. Add integration test that makes sure of the rollback in db
    public void signUp(SignUp signUp) throws ResponseStatusException {
        throwConflictIfFound(signUp);
        signUpWriteRepository.save(signUp);
        // TODO change this to some kind of internal event so we don't need to access to all players usecases modules
        createTeamUseCase.create(signUp.getTeamName(), 50, signUp.getId());
    }

    private void throwConflictIfFound(SignUp signUp) {
        signUpReadRepository.findByUsernameIgnoreCase(signUp.getUsername())
            .ifPresent(conflictingSignUp -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Username %s already in use", conflictingSignUp.getUsername()));
            });
    }
}
