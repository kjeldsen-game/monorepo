package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.application.usecases.email.SendEmailUseCase;
import com.kjeldsen.auth.domain.PasswordResetToken;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.repositories.PasswordResetTokenWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class GeneratePasswordResetLinkUseCaseTest {

    private final SendEmailUseCase mockedSendEmailUseCase = Mockito.mock(SendEmailUseCase.class);
    private final GetUserUseCase mockedGetUserUseCase = Mockito.mock(GetUserUseCase.class);
    private final PasswordResetTokenWriteRepository mockedPasswordResetTokenWriteRepository = Mockito.mock(PasswordResetTokenWriteRepository.class);
    private final GeneratePasswordResetLinkUseCase generatePasswordResetLinkUseCase = new GeneratePasswordResetLinkUseCase(
        mockedSendEmailUseCase, mockedGetUserUseCase, mockedPasswordResetTokenWriteRepository);

    @Test
    @DisplayName("Should generate reset link and send mail")
    void should_generate_reset_link_and_send_mail() {
        User testUser = User.builder().id("id").email("email@email.com").build();
        when(mockedGetUserUseCase.getUserByEmail("email@email.com")).thenReturn(testUser);

        generatePasswordResetLinkUseCase.generate(testUser.getEmail());
        verify(mockedPasswordResetTokenWriteRepository, times(1)).save(
            Mockito.any(PasswordResetToken.class));
        verify(mockedSendEmailUseCase, times(1)).sendSimpleEmail(
            eq(testUser.getEmail()), eq("Password Reset Link"), any(String.class));
    }
}