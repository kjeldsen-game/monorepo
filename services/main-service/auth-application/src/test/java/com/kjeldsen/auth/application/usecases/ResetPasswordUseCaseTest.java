package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.PasswordResetToken;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.exceptions.UnauthorizedException;
import com.kjeldsen.auth.domain.repositories.PasswordResetTokenReadRepository;
import com.kjeldsen.auth.domain.repositories.PasswordResetTokenWriteRepository;
import com.kjeldsen.auth.domain.repositories.UserWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class ResetPasswordUseCaseTest {

    private final GetUserUseCase mockedGetUserUseCase = Mockito.mock(GetUserUseCase.class);
    private final PasswordResetTokenReadRepository mockedPasswordResetTokenReadRepository = Mockito.mock(PasswordResetTokenReadRepository.class);
    private final PasswordResetTokenWriteRepository mockedPasswordResetTokenWriteRepository = Mockito.mock(PasswordResetTokenWriteRepository.class);
    private final UserWriteRepository mockedUserWriteRepository = Mockito.mock(UserWriteRepository.class);
    private final ResetPasswordUseCase resetPasswordUseCase = new ResetPasswordUseCase(
        mockedGetUserUseCase, mockedPasswordResetTokenReadRepository, mockedPasswordResetTokenWriteRepository
        ,mockedUserWriteRepository);

    @Test
    @DisplayName("Should throw and error when token don't exists")
    void should_throw_and_error_when_token_dont_exists() {

        when(mockedPasswordResetTokenReadRepository.findByToken("token")).thenReturn(Optional.empty());

        assertEquals("Invalid token!", assertThrows(UnauthorizedException.class,
            () -> resetPasswordUseCase.resetPassword("token", "pass", "pass"))
            .getMessage());
    }

    @Test
    @DisplayName("Should throw error when token expired")
    void should_throw_error_when_token_expired() {
        PasswordResetToken mockedPasswordResetToken = Mockito.mock(PasswordResetToken.class);
        when(mockedPasswordResetTokenReadRepository.findByToken("token")).thenReturn(Optional.of(
            mockedPasswordResetToken));
        when(mockedPasswordResetToken.getExpiryDate()).thenReturn(Instant.now().minusSeconds(1));
        assertEquals("Invalid token!", assertThrows(UnauthorizedException.class,
            () -> resetPasswordUseCase.resetPassword("token", "pass", "pass")).getMessage());
    }

    @Test
    @DisplayName("Should update the user password and delete token")
    void should_update_password_and_delete_token() {
        User testUser = User.builder().id("userId").build();
        PasswordResetToken mockedPasswordResetToken = PasswordResetToken.builder().token("token").userId("userId").build();
        when(mockedPasswordResetTokenReadRepository.findByToken("token")).thenReturn(Optional.of(
            mockedPasswordResetToken));
        when(mockedGetUserUseCase.getUserById("userId")).thenReturn(testUser);
        resetPasswordUseCase.resetPassword("token", "pass", "pass");
        assertEquals("pass", testUser.getPassword());

        verify(mockedPasswordResetTokenWriteRepository, times(1))
            .delete(mockedPasswordResetToken.getToken());
        verify(mockedUserWriteRepository, times(1)).save(testUser);
    }
}