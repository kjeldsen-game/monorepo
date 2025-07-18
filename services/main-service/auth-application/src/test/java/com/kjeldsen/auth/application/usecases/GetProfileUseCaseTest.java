package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.Profile;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.exceptions.NotFoundException;
import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.model.team.TeamClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetProfileUseCaseTest {

    private final GetUserUseCase mockedGetUserUseCase = Mockito.mock(GetUserUseCase.class);
    private final TeamClientApi mockedTeamClientApi = Mockito.mock(TeamClientApi.class);
    private final GetProfileUseCase getProfileUseCase = new GetProfileUseCase(mockedGetUserUseCase, mockedTeamClientApi);

    @Test
    @DisplayName("Should throw error when team not found")
    void should_throw_error_when_team_not_found() {
        User user = Mockito.mock(User.class);
        when(mockedGetUserUseCase.getCurrent()).thenReturn(user);
        when(user.getId()).thenReturn("userId");
        when(user.getEmail()).thenReturn("email");
        when(mockedTeamClientApi.getTeam(null, null, "userId")).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, getProfileUseCase::get);
    }

    @Test
    @DisplayName("Should return the profile object")
    void should_return_profile_object() {
        User user = User.builder().id("userId").email("email").avatar("exampleAvatarBytes".getBytes()).build();
        when(mockedGetUserUseCase.getCurrent()).thenReturn(user);
        when(mockedTeamClientApi.getTeam(null, null, "userId")).thenReturn(List.of(
            TeamClient.builder().name("teamName").build()
        ));

        Profile profile = getProfileUseCase.get();
        assertThat(profile).isNotNull();
        assertThat(profile.getEmail()).isEqualTo("email");
        assertThat(profile.getTeamName()).isEqualTo("teamName");
        assertThat(profile.getAvatar()).isOfAnyClassIn(String.class);
    }
}