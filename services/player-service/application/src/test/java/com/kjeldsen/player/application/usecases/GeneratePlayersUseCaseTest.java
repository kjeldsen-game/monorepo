package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.repositories.player.PlayerPositionTendencyReadRepository;
import com.kjeldsen.player.domain.repositories.player.PlayerWriteRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class GeneratePlayersUseCaseTest {

    private final PlayerWriteRepository mockedPlayerWriteRepository = mock(PlayerWriteRepository.class);
    final private PlayerPositionTendencyReadRepository mockedPlayerPositionTendencyReadRepository = mock(
        PlayerPositionTendencyReadRepository.class);
    private final GeneratePlayersUseCase generatePlayersUseCase = new GeneratePlayersUseCase(mockedPlayerWriteRepository,
        mockedPlayerPositionTendencyReadRepository);

    @Test
    @DisplayName("create N players randomly in the given age range, position and total points distributed in the actual skills")
    void create_n_players_randomly_in_the_given_age_range_position_and_total_points_distributed_in_the_actual_skills() {
        Team.TeamId teamId = Team.TeamId.of("teamId");
        when(mockedPlayerPositionTendencyReadRepository.get(any(PlayerPosition.class)))
            .thenReturn(PlayerPositionTendency.DEFAULT_CENTRE_BACK_TENDENCIES,
                PlayerPositionTendency.DEFAULT_AERIAL_CENTRE_BACK_TENDENCIES,
                PlayerPositionTendency.DEFAULT_FULL_BACK_TENDENCIES,
                PlayerPositionTendency.DEFAULT_FULL_WINGBACK_TENDENCIES,
                PlayerPositionTendency.DEFAULT_DEFENSIVE_MIDFIELDER_TENDENCIES,
                PlayerPositionTendency.DEFAULT_CENTRE_MIDFIELDER_TENDENCIES,
                PlayerPositionTendency.DEFAULT_OFFENSIVE_MIDFIELDER_TENDENCIES,
                PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES,
                PlayerPositionTendency.DEFAULT_AERIAL_FORWARD_TENDENCIES,
                PlayerPositionTendency.DEFAULT_GOALKEEPER_TENDENCIES);

        generatePlayersUseCase.generate(10, teamId);

        ArgumentCaptor<Player> argumentCaptor = ArgumentCaptor.forClass(Player.class);
        verify(mockedPlayerWriteRepository, times(10))
            .save(argumentCaptor.capture());

        List<Player> playersToSave = argumentCaptor.getAllValues();

        assertFalse(playersToSave.isEmpty());
        assertThat(playersToSave).allMatch(player ->
            player.getAge() >= 15
                && player.getAge() <= 33
                && StringUtils.isNotBlank(player.getName())
                && player.getTeamId().equals(teamId)
                && player.getActualSkills().values().stream().map(PlayerSkills::getActual).mapToInt(Integer::intValue).sum() == 200);
    }
}
