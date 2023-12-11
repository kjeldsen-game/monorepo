package com.kjeldsen.player.test.delegate;

import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.rest.delegate.TeamDelegate;
import com.kjeldsen.player.rest.model.TeamResponse;
import com.kjeldsen.security.AuthenticationFetcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TeamDelegateTest {

    private final GetTeamUseCase mockedGetTeamUseCase = Mockito.mock(GetTeamUseCase.class);
    private final AuthenticationFetcher mockedAuthenticationFetcher = Mockito.mock(AuthenticationFetcher.class);
    private final TeamReadRepository teamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final TeamDelegate teamDelegate = new TeamDelegate(mockedGetTeamUseCase, mockedAuthenticationFetcher, teamReadRepository);

    @Test
    public void should_return_a_team_response_on_getTeam_method() {
        Team team = Team.builder()
            .id(Team.TeamId.of("exampleId"))
            .name("exampleName")
            .userId("exampleUserId")
            .cantera(Team.Cantera.builder().score(.0).build())
            .build();

        when(mockedAuthenticationFetcher.getLoggedUserID()).thenReturn("123");
        when(mockedGetTeamUseCase.get(mockedAuthenticationFetcher.getLoggedUserID())).thenReturn(team);
        ResponseEntity<TeamResponse> responseEntity = teamDelegate.getTeam();

        verify(mockedGetTeamUseCase, times(1)).get(mockedAuthenticationFetcher.getLoggedUserID());
        verify(mockedAuthenticationFetcher, times(3)).getLoggedUserID();

        Assertions.assertNotNull(responseEntity);
        Assertions.assertTrue(responseEntity instanceof ResponseEntity<TeamResponse>);
    }
}
