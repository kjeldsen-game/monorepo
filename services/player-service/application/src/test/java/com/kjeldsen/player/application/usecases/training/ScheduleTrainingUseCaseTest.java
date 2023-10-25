package com.kjeldsen.player.application.usecases.training;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventWriteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class ScheduleTrainingUseCaseTest {

    private static final Player.PlayerId PLAYER_ID = Player.PlayerId.generate();

    @Mock
    private PlayerTrainingScheduledEventWriteRepository playerTrainingScheduledEventWriteRepository;
    @Mock
    private PlayerReadRepository playerReadRepository;
    @InjectMocks
    private ScheduleTrainingUseCase scheduleTrainingUseCase;

    @Test
    void should_throw_an_error_when_training_days_is_out_of_range() {
        Assertions.assertThatThrownBy(() -> scheduleTrainingUseCase.generate(PLAYER_ID, PlayerSkill.BALL_CONTROL, 1001))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Days must be between 1 and 1000");
    }

    @Test
    void should_throw_an_error_when_player_not_found() {
        Mockito.when(playerReadRepository.findOneById(PLAYER_ID)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> scheduleTrainingUseCase.generate(PLAYER_ID, PlayerSkill.BALL_CONTROL, 500))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Player not found.");
    }

    @Test
    void should_save_a_player_training_scheduled_event() {
        Player player = Player.builder().id(PLAYER_ID).build();

        Mockito.when(playerReadRepository.findOneById(PLAYER_ID)).thenReturn(Optional.of(player));

        scheduleTrainingUseCase.generate(PLAYER_ID, PlayerSkill.BALL_CONTROL, 500);

        Mockito.verify(playerTrainingScheduledEventWriteRepository)
            .save(Mockito.argThat(event -> event.getPlayerId().equals(PLAYER_ID)
                && event.getSkill().equals(PlayerSkill.BALL_CONTROL)
                && event.getTrainingDays().equals(500)));
        verifyNoMoreInteractions(playerReadRepository, playerTrainingScheduledEventWriteRepository);
    }

}
