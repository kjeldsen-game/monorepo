package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.exceptions.BadRequestException;
import com.kjeldsen.auth.domain.exceptions.UnauthorizedException;
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
        user.setEmail("email@email.com");
        user.setId("id");
        when(mockedGetUserUseCase.getUserByEmail("email@email.com")).thenReturn(user);
        when(mockedPasswordEncoder.matches(inputPassword, user.getPassword())).thenReturn(false);
        RuntimeException exception = assertThrows(UnauthorizedException.class,
            () -> generateTokenUseCase.get("email@email.com", "password"));
        assertEquals("Invalid email or password!", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception email is not in mail format")
    void should_throw_exception_when_email_is_not_in_mail_format() {
        String inputPassword = "password";
        User user = new User();
        user.setEmail("email");
        user.setId("id");
        when(mockedGetUserUseCase.getUserByEmail("email")).thenReturn(user);
        when(mockedPasswordEncoder.matches(inputPassword, user.getPassword())).thenReturn(false);
        RuntimeException exception = assertThrows(BadRequestException.class,
            () -> generateTokenUseCase.get("email", "password"));
        assertEquals("Invalid email address format!", exception.getMessage());
    }

    @Test
    @DisplayName("Should return token")
    void should_return_token() {
        User user = new User();
        user.setPassword("password");
        user.setEmail("email@email.com");
        user.setId("id");
        when(mockedGetUserUseCase.getUserByEmail("email@email.com")).thenReturn(user);
        when(mockedPasswordEncoder.matches("password", user.getPassword())).thenReturn(true);

        generateTokenUseCase.get("email@email.com", "password");

        verify(mockedGetUserUseCase,times(1)).getUserByEmail("email@email.com");
        verify(mockedPasswordEncoder,times(1)).matches(any(), any());
    }
}