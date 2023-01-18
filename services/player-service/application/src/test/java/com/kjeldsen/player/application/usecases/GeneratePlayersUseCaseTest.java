package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.repositories.PlayerPositionTendencyReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GeneratePlayersUseCaseTest {

    private final PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    final private PlayerPositionTendencyReadRepository mockedPlayerPositionTendencyReadRepository = Mockito.mock(
        PlayerPositionTendencyReadRepository.class);
    private final GeneratePlayersUseCase generatePlayersUseCase = new GeneratePlayersUseCase(mockedPlayerWriteRepository,
        mockedPlayerPositionTendencyReadRepository);

    @Test
    @DisplayName("create N players randomly in the given age range, position and total points distributed in the actual skills")
    public void create_n_players_randomly_in_the_given_age_range_position_and_total_points_distributed_in_the_actual_skills() {
        Mockito.when(mockedPlayerPositionTendencyReadRepository.get(Mockito.any(PlayerPosition.class)))
            .thenReturn(PlayerPositionTendency.DEFAULT_DEFENDER_TENDENCIES,
                PlayerPositionTendency.DEFAULT_MIDDLE_TENDENCIES,
                PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES);

        generatePlayersUseCase.generate(10);

        ArgumentCaptor<Player> argumentCaptor = ArgumentCaptor.forClass(Player.class);
        Mockito.verify(mockedPlayerWriteRepository, Mockito.times(10)).save(argumentCaptor.capture());

        List<Player> playersToSave = argumentCaptor.getAllValues();

        assertThat(playersToSave).allMatch(player ->
            player.getAge().value() >= 15
                && player.getAge().value() <= 33
                && player.getActualSkills().getTotalPoints() == 200);
    }
}
