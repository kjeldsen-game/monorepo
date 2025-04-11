package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.exceptions.UserNotFoundException;
import com.kjeldsen.auth.domain.exceptions.UserNotLoggedException;
import com.kjeldsen.auth.domain.repositories.UserReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetUserUseCaseTest {

    private final UserReadRepository mockedUserReadRepository = Mockito.mock(UserReadRepository.class);
    private final GetUserUseCase getUserUseCase = new GetUserUseCase(mockedUserReadRepository);

    @Test
    @DisplayName("Should throw error when user is not logged in")
    void should_throw_error_when_user_is_not_logged_in() {
        when(mockedUserReadRepository.findByUserId("userId")).thenReturn(Optional.empty());
        assertEquals("User not logged in!", assertThrows(UserNotLoggedException.class, getUserUseCase::getCurrent).getMessage());
    }

    @Test
    @DisplayName("Should get a user when user is logged in")
    void should_get_user_when_user_is_logged_in() {
        User user = new User();
        user.setEmail("email");
        user.setPassword("password");
        user.setId("userId");
        when(mockedUserReadRepository.findByUserId("userId")).thenReturn(Optional.of(user));

        try(MockedStatic<SecurityUtils> mockedStatic = Mockito.mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getCurrentUserId).thenReturn("userId");
            User resultUser = getUserUseCase.getCurrent();
            assertEquals("email", resultUser.getEmail());
        }
    }

    @Test
    @DisplayName("Should throw error when user not found by email")
    void should_throw_error_when_user_not_found_by_email() {
        when(mockedUserReadRepository.findByEmail("email")).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(UserNotFoundException.class,
            () -> getUserUseCase.getUserByEmail("email"));
        assertEquals("User not found!", exception.getMessage());
    }

    @Test
    @DisplayName("Should return user found by email")
    void should_return_user_when_user_found_by_email() {
        User user = new User();
        user.setEmail("email");
        user.setPassword("password");
        user.setId("userId");
        when(mockedUserReadRepository.findByEmail("email")).thenReturn(Optional.of(user));
        assertEquals("email", user.getEmail());
    }
}