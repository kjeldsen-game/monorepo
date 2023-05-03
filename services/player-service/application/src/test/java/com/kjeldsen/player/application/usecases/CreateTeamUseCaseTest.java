package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateTeamUseCaseTest {

    private final TeamWriteRepository mockedTeamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final GeneratePlayersUseCase mockedGeneratePlayersUseCase = Mockito.mock(GeneratePlayersUseCase.class);
    private final CreateTeamUseCase createTeamUseCase = new CreateTeamUseCase(mockedGeneratePlayersUseCase, mockedTeamWriteRepository);

    @Test
    @DisplayName("should create new team with generated players")
    void should_create_new_team_with_generated_players() {
        String teamName = "Team Name";
        int numberOfPlayers = 1;
        String userId = UUID.randomUUID().toString();
        Player player = PlayerProvider.generate(Team.TeamId.generate(), PlayerPositionTendency.DEFAULT_DEFENDER_TENDENCIES, 200);

        when(mockedGeneratePlayersUseCase.generate(anyInt(), any()))
            .thenReturn(List.of(player));

        createTeamUseCase.create(teamName, numberOfPlayers, userId);

        verify(mockedTeamWriteRepository)
            .save(
                argThat(team -> !Objects.isNull(team.getId())
                    && team.getName().equals(teamName)
                    && team.getPlayers().size() == 1
                    && team.getPlayers().get(0).equals(player)),
                eq(userId)
            );
    }
}
