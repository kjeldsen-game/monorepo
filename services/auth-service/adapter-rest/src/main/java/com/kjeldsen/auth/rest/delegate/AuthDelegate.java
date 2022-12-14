package com.kjeldsen.auth.rest.delegate;

import com.kjeldsen.auth.domain.SignUp;
import com.kjeldsen.auth.rest.api.AuthApiDelegate;
import com.kjeldsen.auth.rest.model.SignUpRequest;
import com.kjeldsen.auth.rest.security.CustomPasswordEncoder;
import com.kjeldsen.auth.usecases.SignUpUseCase;
import com.kjeldsen.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthDelegate implements AuthApiDelegate {

    private final CustomPasswordEncoder customPasswordEncoder;
    private final SignUpUseCase signUpUseCase;
    private final SecurityUtils securityUtils;

    @Override
    public ResponseEntity<Void> signup(SignUpRequest signUpRequest) {

        SignUp signUp = SignUp.builder()
            .username(signUpRequest.getUsername())
            .passwordHash(customPasswordEncoder.encode(signUpRequest.getPassword()))
            .build();

        signUpUseCase.signUp(signUp);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    // TODO to be deleted. Just here to test that the library works :D
    private void testingLib() {
        securityUtils.getLoggedUserID();
        securityUtils.getLoggedUserUsername();
    }


}