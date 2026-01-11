package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.models.User;
import com.kjeldsen.auth.domain.repositories.UserWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateAvatarUseCaseTest {


    private final GetUserUseCase mockedGetUserUseCase = Mockito.mock(GetUserUseCase.class);
    private final UserWriteRepository mockedUserWriteRepository = Mockito.mock(UserWriteRepository.class);
    private final MultipartFile mockedMultipartFile = Mockito.mock(MultipartFile.class);
    private final UpdateAvatarUseCase updateAvatarUseCase = new UpdateAvatarUseCase(mockedGetUserUseCase, mockedUserWriteRepository);

    @Test
    @DisplayName("Should throw exception from getBytes")
    void should_throw_exception_from_getBytes() throws IOException {
        User user = User.builder().build();
        user.setId("user-id");
        when(mockedGetUserUseCase.getCurrent()).thenReturn(user);
        when(mockedMultipartFile.getBytes()).thenThrow(new IOException("Simulated IO failure"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
            updateAvatarUseCase.updateAvatar(mockedMultipartFile));
        assertTrue(thrown.getMessage().contains("Failed to update avatar"));
        assertInstanceOf(IOException.class, thrown.getCause());
    }

    @Test
    @DisplayName("Should set new avatar bytes value")
    void should_set_avatar_bytes_value() throws IOException {
        User user = Mockito.mock(User.class);
        when(mockedGetUserUseCase.getCurrent()).thenReturn(user);
        when(user.getId()).thenReturn("user-id");
        when(mockedMultipartFile.getBytes()).thenReturn("example".getBytes());

        updateAvatarUseCase.updateAvatar(mockedMultipartFile);
        verify(mockedUserWriteRepository).save(user);
        verify(user).setAvatar("example".getBytes());
    }
}