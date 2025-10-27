package com.kjeldsen.player.application.usecases.team;

import com.kjeldsen.lib.events.TeamCreationEvent;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.player.application.usecases.GeneratePlayersUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateBotTeamsUseCaseTest {
    @Mock
    private GeneratePlayersUseCase mockedGeneratePlayersUseCase;
    @Mock
    private TeamWriteRepository mockedTeamWriteRepository;
    @Mock
    private PlayerWriteRepository mockedPlayerWriteRepository;
    @Mock
    private GenericEventPublisher mockedGenericEventPublisher;
    @InjectMocks
    private  CreateBotTeamsUseCase createBotTeamsUseCase;
    @Test
    @DisplayName("Should throw error when leagueId is null")
    void should_throw_error_when_leagueIdIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
            createBotTeamsUseCase.create(11, null));
    }

    @Test
    @DisplayName("Should create bot teams")
    void should_create_bot_teams() {
        int botCount = 11;
        createBotTeamsUseCase.create(botCount, "league124");
        Mockito.verify(mockedTeamWriteRepository, Mockito.times(botCount)).save(Mockito.any(Team.class));
        Mockito.verify(mockedGeneratePlayersUseCase, Mockito.times(botCount))
            .generateCustomPlayers(Mockito.any(Team.TeamId.class));

        Mockito.verify(mockedGenericEventPublisher, Mockito.times(1))
            .publishEvent(TeamCreationEvent.builder().isBots(true).teams(Mockito.any()).leagueId("league124").build());
    }
}