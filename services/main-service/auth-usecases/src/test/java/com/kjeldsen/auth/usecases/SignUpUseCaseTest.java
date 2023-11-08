package com.kjeldsen.auth.usecases;

import com.kjeldsen.auth.domain.SignUp;
import com.kjeldsen.auth.persistence.SignUpReadRepository;
import com.kjeldsen.auth.persistence.SignUpWriteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SignUpUseCaseTest {

    private final SignUpWriteRepository mockSignUpWriteRepository = mock(SignUpWriteRepository.class);
    private final SignUpReadRepository mockSignUpReadRepository = mock(SignUpReadRepository.class);
    private final SignUpUseCase signUpUseCase = new SignUpUseCase(mockSignUpWriteRepository, mockSignUpReadRepository);

    @Test
    void should_sign_up_user_correctly() {
        SignUp signUp = SignUp.builder()
            .username("username")
            .passwordHash("passwordHash")
            .teamName("teamName")
            .build();

        signUpUseCase.signUp(signUp);

        verify(mockSignUpWriteRepository).save(signUp);
    }

    @Test
    void should_throw_exception_if_username_already_exists() {
        SignUp signUp = SignUp.builder()
            .username("john.doe")
            .passwordHash("changeit")
            .teamName("John Doe's Team")
            .build();

        when(mockSignUpReadRepository.findByUsernameIgnoreCase(signUp.getUsername())).thenReturn(java.util.Optional.of(signUp));

        assertThatThrownBy(() -> signUpUseCase.signUp(signUp)).isInstanceOf(ResponseStatusException.class).hasMessage("409 CONFLICT \"Username john.doe already in use\"");

        verify(mockSignUpWriteRepository, times(0)).save(signUp);
    }
}