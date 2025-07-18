package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.exceptions.BadRequestException;
import com.kjeldsen.auth.domain.exceptions.UnauthorizedException;
import com.kjeldsen.auth.domain.repositories.UserWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChangePasswordUseCaseTest {

    private final GetUserUseCase mockedGetUserUseCase = Mockito.mock(GetUserUseCase.class);
    private final UserWriteRepository mockedUserWriteRepository = Mockito.mock(UserWriteRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    private final ChangePasswordUseCase changePasswordUseCase = new ChangePasswordUseCase(
        mockedGetUserUseCase, mockedUserWriteRepository, passwordEncoder);

    @Test
    @DisplayName("Should throw error when old pass does not match")
    void should_throw_error_when_old_pass_does_not_match() {
        User testUser = User.builder().id("id").password(passwordEncoder.encode("password")).build();
        when(mockedGetUserUseCase.getCurrent()).thenReturn(testUser);
        assertEquals("Old passwords does not match!", assertThrows(UnauthorizedException.class, () -> {
            changePasswordUseCase.changePassword("password1", "password", "password");
        }).getMessage());
    }

    @Test
    @DisplayName("Should throw error when new passwords does not match")
    void should_throw_error_when_new_passwords_does_not_match() {
        User testUser = User.builder().id("id").password(passwordEncoder.encode("password")).build();
        when(mockedGetUserUseCase.getCurrent()).thenReturn(testUser);
        assertEquals("New passwords do not match!", assertThrows(BadRequestException.class, () -> {
            changePasswordUseCase.changePassword("password", "password1", "password");
        }).getMessage());
    }

    @Test
    @DisplayName("Should change user's password")
    void should_change_user_password() {
        User testUser = User.builder().id("id")
            .password(passwordEncoder.encode("password")).build();
        when(mockedGetUserUseCase.getCurrent()).thenReturn(testUser);

        System.out.println(testUser);

        changePasswordUseCase.changePassword("password", "password1", "password1");
        
        assertThat(passwordEncoder.matches("password1", testUser.getPassword())).isTrue();
        verify(mockedUserWriteRepository).save(testUser);
    }
}
