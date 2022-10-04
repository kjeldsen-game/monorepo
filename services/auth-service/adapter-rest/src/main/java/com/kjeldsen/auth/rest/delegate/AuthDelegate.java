package com.kjeldsen.auth.rest.delegate;

import com.kjeldsen.auth.rest.api.AuthApiDelegate;
import com.kjeldsen.auth.rest.model.SignUpRequest;
import com.kjeldsen.auth.rest.security.CustomPasswordEncoder;
import domain.events.SignUpEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import usecases.SignUpUseCase;

@Component
@RequiredArgsConstructor
public class AuthDelegate implements AuthApiDelegate {

    private final CustomPasswordEncoder customPasswordEncoder;
    private final SignUpUseCase signUpUseCase;

    @Override
    public ResponseEntity<Void> signup(SignUpRequest signUpRequest) {

        SignUpEvent signUpEvent = SignUpEvent.builder()
            .username(signUpRequest.getUsername())
            .passwordHash(customPasswordEncoder.encode(signUpRequest.getPassword()))
            .build();

        signUpUseCase.signUp(signUpEvent);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}