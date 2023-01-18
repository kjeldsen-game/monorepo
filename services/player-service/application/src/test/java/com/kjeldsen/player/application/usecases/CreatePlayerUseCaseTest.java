package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.repositories.PlayerPositionTendencyReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class CreatePlayerUseCaseTest {

    final private PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    final private PlayerPositionTendencyReadRepository mockedPlayerPositionTendencyReadRepository = Mockito.mock(
        PlayerPositionTendencyReadRepository.class);
    final private CreatePlayerUseCase createPlayerUseCase = new CreatePlayerUseCase(mockedPlayerWriteRepository,
        mockedPlayerPositionTendencyReadRepository);

    @Test
    @DisplayName("create a player with the given age, position and total points distributed in the actual skills")
    void create_a_player_with_the_given_age_position_and_total_points_distributed_in_the_actual_skills() {
        NewPlayer newPlayer = NewPlayer.builder()
            .age(PlayerAge.of(20))
            .position(PlayerPosition.MIDDLE)
            .points(200)
            .build();
        Mockito.when(mockedPlayerPositionTendencyReadRepository.get(Mockito.eq(PlayerPosition.MIDDLE)))
            .thenReturn(PlayerPositionTendency.DEFAULT_MIDDLE_TENDENCIES);

        createPlayerUseCase.create(newPlayer);

        ArgumentCaptor<Player> argumentCaptor = ArgumentCaptor.forClass(Player.class);
        Mockito.verify(mockedPlayerWriteRepository, Mockito.times(1)).save(argumentCaptor.capture());

        Player playerToSave = argumentCaptor.getValue();
        assertThat(playerToSave)
            .matches(player -> player.getAge().equals(PlayerAge.of(20))
                && player.getPosition().equals(PlayerPosition.MIDDLE)
                && StringUtils.isNotBlank(player.getName().value())
                && player.getActualSkills().getTotalPoints() == 200);
    }
}
