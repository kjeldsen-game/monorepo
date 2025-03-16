package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.exceptions.InvalidPasswordException;
import com.kjeldsen.auth.domain.providers.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenerateTokenUseCaseTest {

    private final GetUserUseCase mockedGetUserUseCase = Mockito.mock(GetUserUseCase.class);
    private final PasswordEncoder mockedPasswordEncoder = Mockito.mock(PasswordEncoder.class);
    private final JwtTokenProvider mockedJwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
    private final GenerateTokenUseCase generateTokenUseCase = new GenerateTokenUseCase(mockedGetUserUseCase,
        mockedPasswordEncoder, mockedJwtTokenProvider);

    @Test
    @DisplayName("Should throw exception when password is not same")
    void should_throw_exception_when_password_is_not_same() {
        String inputPassword = "password";
        User user = new User();
        user.setPassword("passwordaaa");
        user.setEmail("email");
        user.setId("id");
        when(mockedGetUserUseCase.getUserByEmail("email")).thenReturn(user);
        when(mockedPasswordEncoder.matches(inputPassword, user.getPassword())).thenReturn(false);
        RuntimeException exception = assertThrows(InvalidPasswordException.class,
            () -> generateTokenUseCase.get("email", "password"));
        assertEquals("Invalid password!", exception.getMessage());
    }

    @Test
    @DisplayName("Should return token")
    void should_return_token() {
        User user = new User();
        user.setPassword("password");
        user.setEmail("email");
        user.setId("id");
        when(mockedGetUserUseCase.getUserByEmail("email")).thenReturn(user);
        when(mockedPasswordEncoder.matches("password", user.getPassword())).thenReturn(true);

        generateTokenUseCase.get("email", "password");

        verify(mockedGetUserUseCase,times(1)).getUserByEmail("email");
        verify(mockedPasswordEncoder,times(1)).matches(any(), any());
    }
}