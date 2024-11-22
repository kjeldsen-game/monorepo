package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.Role;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.repositories.UserReadRepository;
import com.kjeldsen.auth.domain.repositories.UserWriteRepository;
import com.kjeldsen.player.application.usecases.CreateTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.FindTeamsQuery;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterUserUseCaseTest {


    private final UserReadRepository userReadRepository = Mockito.mock(UserReadRepository.class);
    private final UserWriteRepository userWriteRepository = Mockito.mock(UserWriteRepository.class);
    private final TeamReadRepository teamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    private final CreateTeamUseCase createTeamUseCase = Mockito.mock(CreateTeamUseCase.class);
    private final RegisterUserUseCase registerUserUseCase = new RegisterUserUseCase(
        userReadRepository, userWriteRepository, teamReadRepository, passwordEncoder, createTeamUseCase);

    @Test
    @DisplayName("Should throw error is email is already Taken")
    public void should_throw_error_is_email_already_taken() {
        when(userReadRepository.findByEmail("email")).thenReturn(Optional.of(new User()));

        assertEquals("Username taken", assertThrows(RuntimeException.class, () -> {
            registerUserUseCase.register("email", "password", "team");
        }).getMessage());
    }

    @Test
    @DisplayName("Should throw error is team name is already Taken")
    public void should_throw_error_is_team_name_already_taken() {
        String teamName = "team";
        when(userReadRepository.findByEmail("email")).thenReturn(Optional.empty());
        when(teamReadRepository.findByTeamName(teamName))
            .thenReturn(Optional.of(Team.builder().name(teamName).build()));

        assertEquals("Team name taken", assertThrows(RuntimeException.class, () -> {
            registerUserUseCase.register("email", "password", teamName);
        }).getMessage());
    }

    @Test
    @DisplayName("Should register user")
    public void should_register_user() {
        when(userReadRepository.findByEmail("email")).thenReturn(Optional.empty());
        when(teamReadRepository.findByTeamName("team"))
            .thenReturn(Optional.empty());

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

        verify(createTeamUseCase, times(1)).create(eq("team"), any(),  any());
    }
}