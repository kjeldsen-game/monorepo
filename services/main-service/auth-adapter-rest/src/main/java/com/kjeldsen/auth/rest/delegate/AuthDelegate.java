package com.kjeldsen.auth.rest.delegate;

import com.kjeldsen.auth.domain.SignUp;
import com.kjeldsen.auth.rest.api.AuthApiDelegate;
import com.kjeldsen.auth.rest.model.SignUpRequest;
import com.kjeldsen.auth.usecases.SignUpUseCase;
import com.kjeldsen.security.CustomPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthDelegate implements AuthApiDelegate {

    private final CustomPasswordEncoder customPasswordEncoder;
    private final SignUpUseCase signUpUseCase;

    @Override
    public ResponseEntity<Void> signup(SignUpRequest signUpRequest) {

        SignUp signUp = SignUp.builder()
            .username(signUpRequest.getUsername())
            .passwordHash(customPasswordEncoder.encode(signUpRequest.getPassword()))
            .teamName(signUpRequest.getTeamName())
            .build();

        signUpUseCase.signUp(signUp);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}