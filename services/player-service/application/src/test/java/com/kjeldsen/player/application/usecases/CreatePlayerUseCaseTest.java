package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.publisher.PlayerPublisher;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerCreationEvent;
import com.kjeldsen.player.domain.repositories.PlayerCreationEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerPositionTendencyReadRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreatePlayerUseCaseTest {

    final private PlayerPublisher playerPublisher = Mockito.mock(PlayerPublisher.class);

    final private PlayerCreationEventWriteRepository playerCreationEventWriteRepository = Mockito.mock(PlayerCreationEventWriteRepository.class);
    final private PlayerPositionTendencyReadRepository mockedPlayerPositionTendencyReadRepository = Mockito.mock(
        PlayerPositionTendencyReadRepository.class);
    final private CreatePlayerUseCase createPlayerUseCase = new CreatePlayerUseCase(playerPublisher, mockedPlayerPositionTendencyReadRepository,
        playerCreationEventWriteRepository);

    @Test
    @DisplayName("create a player with the given age, position and total points distributed in the actual skills")
    void create_a_player_with_the_given_age_position_and_total_points_distributed_in_the_actual_skills() {
        Team.TeamId teamId = Team.TeamId.of("teamId");
        CreatePlayerUseCase.NewPlayer newPlayer = CreatePlayerUseCase.NewPlayer.builder()
            .age(20)
            .position(PlayerPosition.MIDDLE)
            .points(200)
            .teamId(teamId)
            .build();
        when(mockedPlayerPositionTendencyReadRepository.get(PlayerPosition.MIDDLE))
            .thenReturn(PlayerPositionTendency.DEFAULT_MIDDLE_TENDENCIES);

        createPlayerUseCase.create(newPlayer);

        ArgumentCaptor<PlayerCreationEvent> argumentCaptor = ArgumentCaptor.forClass(PlayerCreationEvent.class);
        verify(playerCreationEventWriteRepository, Mockito.times(1))
            .save(argumentCaptor.capture());

        PlayerCreationEvent playerCreationEvent = argumentCaptor.getValue();
        assertThat(playerCreationEvent)
            .matches(player -> player.getAge().equals(20)
                && player.getPosition().equals(PlayerPosition.MIDDLE)
                && StringUtils.isNotBlank(player.getName())
                && player.getTeamId().equals(teamId)
                && player.getInitialSkills().values().stream().mapToInt(Integer::intValue).sum() == 200);
    }
}
