package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.Role;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.exceptions.BadRequestException;
import com.kjeldsen.auth.domain.repositories.UserReadRepository;
import com.kjeldsen.auth.domain.repositories.UserWriteRepository;
import com.kjeldsen.lib.TeamClientApiImpl;
import com.kjeldsen.lib.events.UserRegisterEvent;
import com.kjeldsen.lib.model.team.TeamClient;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.player.rest.model.TeamResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterUserUseCaseTest {


    private final UserReadRepository userReadRepository = Mockito.mock(UserReadRepository.class);
    private final UserWriteRepository userWriteRepository = Mockito.mock(UserWriteRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    private final TeamClientApiImpl mockedTeamClientAuth = Mockito.mock(TeamClientApiImpl.class);
    private final GenericEventPublisher mockedUserRegisterPublisher = Mockito.mock(GenericEventPublisher.class);
    private final RegisterUserUseCase registerUserUseCase = new RegisterUserUseCase(
        userReadRepository, userWriteRepository, passwordEncoder, mockedTeamClientAuth, mockedUserRegisterPublisher);

    @Test
    @DisplayName("Should throw error when email is in wrong format")
    void should_throw_error_when_email_is_wrong_format() {
        when(userReadRepository.findByEmail("emailemail.com")).thenReturn(Optional.of(User.builder().build()));

        assertEquals("Invalid email address format!", assertThrows(BadRequestException.class, () -> {
            registerUserUseCase.register("emailemail.com", "password", "team", "password");
        }).getMessage());
    }

    @Test
    @DisplayName("Should throw error when passwords don't match")
    void should_throw_error_when_passwords_do_not_match() {
        when(userReadRepository.findByEmail("email@email.com")).thenReturn(Optional.of(User.builder().build()));

        assertEquals("Passwords do not match!", assertThrows(BadRequestException.class, () -> {
            registerUserUseCase.register("email@email.com", "password", "team", "pass");
        }).getMessage());
    }

    @Test
    @DisplayName("Should throw error is email is already Taken")
    void should_throw_error_is_email_already_taken() {
        when(userReadRepository.findByEmail("email@email.com")).thenReturn(Optional.of(User.builder().build()));

        assertEquals("Email taken!", assertThrows(BadRequestException.class, () -> {
            registerUserUseCase.register("email@email.com", "password", "team", "password");
        }).getMessage());
    }

    @Test
    @DisplayName("Should throw error is team name is already Taken")
    void should_throw_error_is_team_name_already_taken() {
        String teamName = "team";
        when(userReadRepository.findByEmail("email@email.com")).thenReturn(Optional.empty());
        when(mockedTeamClientAuth.getTeams( teamName, null))
            .thenReturn(List.of(new TeamResponse().name(teamName)));
        assertEquals("Team name taken!", assertThrows(BadRequestException.class, () -> {
            registerUserUseCase.register("email@email.com", "password", teamName, "password");
        }).getMessage());
    }

    @Test
    @DisplayName("Should register user")
    void should_register_user() {
        when(userReadRepository.findByEmail("email@email.com")).thenReturn(Optional.empty());
        when(mockedTeamClientAuth.getTeams("team", null))
            .thenReturn(Collections.emptyList());

        User savedUser = User.builder().build();
        savedUser.setId(java.util.UUID.randomUUID().toString());
        savedUser.setEmail("email@email.com");
        savedUser.setPassword(passwordEncoder.encode("password"));
        savedUser.setRoles(Set.of(Role.USER));

        when(userWriteRepository.save(any(User.class))).thenReturn(savedUser);

        registerUserUseCase.register("email@email.com", "password", "team", "password");
        verify(userWriteRepository, times(1)).save(argThat(user -> {
            assertEquals("email@email.com", user.getEmail());
            assertEquals(Set.of(Role.USER), user.getRoles());
            assertTrue(passwordEncoder.matches("password", user.getPassword()));
            return true;
        }));

        verify(mockedUserRegisterPublisher, times(1))
            .publishEvent(any(UserRegisterEvent.class));
    }
}