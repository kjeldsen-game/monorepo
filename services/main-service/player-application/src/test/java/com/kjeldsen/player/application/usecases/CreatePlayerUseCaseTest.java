package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.repositories.PlayerPositionTendencyReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class CreatePlayerUseCaseTest {

    private final PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    final private PlayerPositionTendencyReadRepository mockedPlayerPositionTendencyReadRepository = Mockito.mock(
        PlayerPositionTendencyReadRepository.class);
    final private CreatePlayerUseCase createPlayerUseCase = new CreatePlayerUseCase(mockedPlayerWriteRepository, mockedPlayerPositionTendencyReadRepository);

    @Test
    @DisplayName("create a player with the given age, position and total points distributed in the actual skills")
    void create_a_player_with_the_given_age_position_and_total_points_distributed_in_the_actual_skills() {
        Team.TeamId teamId = Team.TeamId.of("teamId");
        CreatePlayerUseCase.NewPlayer newPlayer = CreatePlayerUseCase.NewPlayer.builder()
            .position(PlayerPosition.CENTRE_MIDFIELDER)
            .points(200)
            .teamId(teamId)
            .build();
        when(mockedPlayerPositionTendencyReadRepository.get(PlayerPosition.CENTRE_MIDFIELDER))
            .thenReturn(PlayerPositionTendency.DEFAULT_CENTRE_MIDFIELDER_TENDENCIES);

        createPlayerUseCase.create(newPlayer);

        verify(mockedPlayerWriteRepository, times(1)).save(any(Player.class));
    }
}
