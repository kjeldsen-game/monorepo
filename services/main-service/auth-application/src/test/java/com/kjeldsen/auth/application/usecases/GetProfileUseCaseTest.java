package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.models.Profile;
import com.kjeldsen.auth.domain.models.User;
import com.kjeldsen.auth.domain.exceptions.NotFoundException;
import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.player.rest.model.TeamResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        when(user.getTeamId()).thenReturn("teamId");
        when(user.getEmail()).thenReturn("email");
        when(mockedTeamClientApi.getTeamById( "teamId")).thenReturn(null);

        assertThrows(NotFoundException.class, getProfileUseCase::get);
    }

    @Test
    @DisplayName("Should return the profile object")
    void should_return_profile_object() {
        User user = User.builder().id("userId").email("email").teamId("123").avatar("exampleAvatarBytes".getBytes()).build();
        when(mockedGetUserUseCase.getCurrent()).thenReturn(user);
        when(mockedTeamClientApi.getTeamById("123")).thenReturn(
            new TeamResponse().name("teamName"));

        Profile profile = getProfileUseCase.get();
        assertThat(profile).isNotNull();
        assertThat(profile.getEmail()).isEqualTo("email");
        assertThat(profile.getTeamName()).isEqualTo("teamName");
        assertThat(profile.getAvatar()).isOfAnyClassIn(String.class);
    }
}