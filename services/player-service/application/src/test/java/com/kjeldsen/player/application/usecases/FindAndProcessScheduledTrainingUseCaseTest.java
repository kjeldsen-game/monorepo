package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.usecases.training.FindAndProcessScheduledTrainingUseCase;
import com.kjeldsen.player.application.usecases.training.GenerateTrainingUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventReadRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


public class FindAndProcessScheduledTrainingUseCaseTest {

    private final PlayerTrainingScheduledEventReadRepository mockPlayerTrainingScheduledEventReadRepository = Mockito.mock(PlayerTrainingScheduledEventReadRepository.class);
    private final GenerateTrainingUseCase generateTrainingUseCase = Mockito.mock(GenerateTrainingUseCase.class);

    private final FindAndProcessScheduledTrainingUseCase findAndProcessScheduledTrainingUseCase = new FindAndProcessScheduledTrainingUseCase(mockPlayerTrainingScheduledEventReadRepository, generateTrainingUseCase);

    @Test
    void should_generate_training_events_per_day() {

        // Given
        LocalDate localDate = LocalDate.now();
        Player.PlayerId playerId = Player.PlayerId.generate();
        PlayerTrainingScheduledEvent playerTrainingScheduledEvent = PlayerTrainingScheduledEvent.builder()
            .playerId(playerId)
            .skill(PlayerSkill.BALL_CONTROL)
            .trainingDays(2)
            .startDate(Instant.now())
            .build();

        PlayerTrainingEvent playerTrainingEvent = PlayerTrainingEvent.builder()
            .playerId(playerId)
            .skill(PlayerSkill.BALL_CONTROL)
            .pointsBeforeTraining(25)
            .points(0)
            .pointsAfterTraining(25)
            .currentDay(1)
            .build();

        Mockito.when(mockPlayerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings(localDate))
            .thenReturn(List.of(playerTrainingScheduledEvent));
        Mockito.when(generateTrainingUseCase.generate(Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(playerTrainingEvent);
        // When
        List<PlayerTrainingEvent> actual = findAndProcessScheduledTrainingUseCase.findAndProcess(localDate);
        // Then
        Assertions.assertThat(actual).hasSize(2);
        Mockito.verify(generateTrainingUseCase).generate(playerId, PlayerSkill.BALL_CONTROL, 1);
        Mockito.verify(generateTrainingUseCase).generate(playerId, PlayerSkill.BALL_CONTROL, 2);
    }

    @Test
    void should_generate_training_events_per_day_resetting_current_day() {

        // Given
        LocalDate localDate = LocalDate.now();
        Player.PlayerId playerId = Player.PlayerId.generate();
        PlayerTrainingScheduledEvent playerTrainingScheduledEvent = PlayerTrainingScheduledEvent.builder()
            .playerId(playerId)
            .skill(PlayerSkill.BALL_CONTROL)
            .trainingDays(2)
            .startDate(Instant.now())
            .build();

        PlayerTrainingEvent playerTrainingEvent = PlayerTrainingEvent.builder()
            .playerId(playerId)
            .skill(PlayerSkill.BALL_CONTROL)
            .pointsBeforeTraining(25)
            .points(1)
            .pointsAfterTraining(26)
            .currentDay(1)
            .build();

        Mockito.when(mockPlayerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings(localDate))
            .thenReturn(List.of(playerTrainingScheduledEvent));
        Mockito.when(generateTrainingUseCase.generate(Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(playerTrainingEvent);
        // When
        List<PlayerTrainingEvent> actual = findAndProcessScheduledTrainingUseCase.findAndProcess(localDate);
        // Then
        Assertions.assertThat(actual).hasSize(2);
        Mockito.verify(generateTrainingUseCase, Mockito.times(2)).generate(playerId, PlayerSkill.BALL_CONTROL, 1);
    }
}
