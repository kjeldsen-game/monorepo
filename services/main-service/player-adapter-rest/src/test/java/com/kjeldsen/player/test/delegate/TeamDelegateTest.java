package com.kjeldsen.player.test.delegate;

import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.application.usecases.economy.GetPlayerWagesTransactionsUseCase;
import com.kjeldsen.player.application.usecases.economy.GetTeamTransactionsUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.rest.delegate.TeamDelegate;
import com.kjeldsen.player.rest.model.TeamResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

public class TeamDelegateTest {

    // TODO mock shiro security utils and test the methods in the delegate

    private final TeamReadRepository teamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final PlayerReadRepository playerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final PlayerWriteRepository playerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    private final GetTeamTransactionsUseCase mockedGetTeamTransactionsUseCase = Mockito.mock(GetTeamTransactionsUseCase.class);
    private final GetPlayerWagesTransactionsUseCase mockedGetPlayerWagesUseCase = Mockito.mock(GetPlayerWagesTransactionsUseCase.class);
//    private final TeamDelegate teamDelegate =
//        new TeamDelegate(
//            mockedGetTeamTransactionsUseCase,
//            teamReadRepository,
//            playerReadRepository,
//            playerWriteRepository,mockedGetPlayerWagesUseCase);

    @Test
    public void should_return_a_team_response_on_getTeam_method() {
        Team team = Team.builder()
            .id(Team.TeamId.of("exampleId"))
            .name("exampleName")
            .userId("exampleUserId")
            .cantera(Team.Cantera.builder().score(.0).build())
            .build();

//        ResponseEntity<TeamResponse> responseEntity = teamDelegate.getTeam();

//        Assertions.assertNotNull(responseEntity);
//        Assertions.assertTrue(responseEntity instanceof ResponseEntity<TeamResponse>);
    }
}
