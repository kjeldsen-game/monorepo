package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.Role;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.exceptions.TeamException;
import com.kjeldsen.auth.domain.exceptions.UsernameException;
import com.kjeldsen.auth.domain.publishers.UserRegisterPublisher;
import com.kjeldsen.auth.domain.repositories.UserReadRepository;
import com.kjeldsen.auth.domain.repositories.UserWriteRepository;
import com.kjeldsen.lib.TeamClientApiImpl;
import com.kjeldsen.lib.events.UserRegisterEvent;
import com.kjeldsen.lib.model.team.TeamClient;
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
    private final UserRegisterPublisher mockedUserRegisterPublisher = Mockito.mock(UserRegisterPublisher.class);
    private final RegisterUserUseCase registerUserUseCase = new RegisterUserUseCase(
        userReadRepository, userWriteRepository, passwordEncoder, mockedTeamClientAuth, mockedUserRegisterPublisher);

    @Test
    @DisplayName("Should throw error is email is already Taken")
    void should_throw_error_is_email_already_taken() {
        when(userReadRepository.findByEmail("email")).thenReturn(Optional.of(new User()));

        assertEquals("Username taken!", assertThrows(UsernameException.class, () -> {
            registerUserUseCase.register("email", "password", "team");
        }).getMessage());
    }

    @Test
    @DisplayName("Should throw error is team name is already Taken")
    void should_throw_error_is_team_name_already_taken() {
        String teamName = "team";
        when(userReadRepository.findByEmail("email")).thenReturn(Optional.empty());
        when(mockedTeamClientAuth.getTeam(null, teamName, null))
            .thenReturn(List.of(TeamClient.builder().name(teamName).build()));
        assertEquals("Team name taken!", assertThrows(TeamException.class, () -> {
            registerUserUseCase.register("email", "password", teamName);
        }).getMessage());
    }

    @Test
    @DisplayName("Should register user")
    void should_register_user() {
        when(userReadRepository.findByEmail("email")).thenReturn(Optional.empty());
        when(mockedTeamClientAuth.getTeam(null,"team", null))
            .thenReturn(Collections.emptyList());

        User savedUser = new User();
        savedUser.setId(java.util.UUID.randomUUID().toString());
        savedUser.setEmail("email");
        savedUser.setPassword(passwordEncoder.encode("password"));
        savedUser.setRoles(Set.of(Role.USER));

        when(userWriteRepository.save(any(User.class))).thenReturn(savedUser);

        registerUserUseCase.register("email", "password", "team");
        verify(userWriteRepository, times(1)).save(argThat(user -> {
            assertEquals("email", user.getEmail());
            assertEquals(Set.of(Role.USER), user.getRoles());
            assertTrue(passwordEncoder.matches("password", user.getPassword()));
            return true;
        }));

        verify(mockedUserRegisterPublisher, times(1))
            .publishUserRegisterEvent(any(UserRegisterEvent.class));
    }
}