package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventWriteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

class ScheduleTrainingUseCaseTest {

    private final PlayerTrainingScheduledEventWriteRepository mockPlayerTrainingScheduledEventWriteRepository = Mockito.mock(PlayerTrainingScheduledEventWriteRepository.class);
    private final PlayerReadRepository mockPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final ScheduleTrainingUseCase scheduleTrainingUseCase = new ScheduleTrainingUseCase(mockPlayerTrainingScheduledEventWriteRepository, mockPlayerReadRepository);

    @Test
    void should_throw_an_error_when_training_days_is_out_of_range() {
        Assertions.assertThatThrownBy(() -> scheduleTrainingUseCase.generate(Player.PlayerId.generate(), PlayerSkill.BALL_CONTROL, 1001))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Days must be between 1 and 1000");
    }

    @Test
    void should_throw_an_error_when_player_not_found() {
        Mockito.when(mockPlayerReadRepository.findOneById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> scheduleTrainingUseCase.generate(Player.PlayerId.generate(), PlayerSkill.BALL_CONTROL, 500))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Player not found.");
    }

    @Test
    void should_save_a_player_training_scheduled_event() {
        Player.PlayerId playerId = Player.PlayerId.generate();
        Player player = Player.builder().id(playerId).build();

        Mockito.when(mockPlayerReadRepository.findOneById(Mockito.any())).thenReturn(Optional.of(player));

        scheduleTrainingUseCase.generate(playerId, PlayerSkill.BALL_CONTROL, 500);

        Mockito.verify(mockPlayerTrainingScheduledEventWriteRepository)
            .save(Mockito.argThat(event -> event.getPlayerId().equals(playerId)
                && event.getSkill().equals(PlayerSkill.BALL_CONTROL)
                && event.getTrainingDays().equals(500)));
    }
}
