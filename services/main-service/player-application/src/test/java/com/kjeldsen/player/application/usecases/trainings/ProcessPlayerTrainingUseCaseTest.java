package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProcessPlayerTrainingUseCaseTest {

    private final PlayerTrainingScheduledEventReadRepository mockedPlayerTrainingScheduledEventReadRepository = Mockito.mock(PlayerTrainingScheduledEventReadRepository.class);
    private final PlayerTrainingEventReadRepository mockedPlayerTrainingEventReadRepository = Mockito.mock(PlayerTrainingEventReadRepository.class);
    private final ExecutePlayerTrainingUseCase mockedExecutePlayerTrainingUseCase = Mockito.mock(ExecutePlayerTrainingUseCase.class);
    private final ProcessPlayerTrainingUseCase processPlayerTrainingUseCase = new ProcessPlayerTrainingUseCase(
        mockedPlayerTrainingScheduledEventReadRepository, mockedPlayerTrainingEventReadRepository, mockedExecutePlayerTrainingUseCase
    );


    @Test
    @DisplayName("Should create a new PlayerTrainingEvent because the Optional is empty")
    public void should_create_a_new_PlayerTrainingEvent_because_optional_is_empty() {
        PlayerTrainingScheduledEvent event =
            PlayerTrainingScheduledEvent.builder()
                .playerId(Player.PlayerId.of("playerId"))
                .id(EventId.generate())
                .skill(PlayerSkill.SCORING)
                .status(PlayerTrainingScheduledEvent.Status.ACTIVE)
                .build();

        when(mockedPlayerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings())
            .thenReturn(List.of(event));
        when(mockedPlayerTrainingEventReadRepository.findLastByPlayerTrainingEvent(event.getId().value()))
            .thenReturn(Optional.empty());

        processPlayerTrainingUseCase.process();

        verify(mockedExecutePlayerTrainingUseCase, Mockito.times(1))
            .execute(event.getPlayerId(), event.getSkill(), 1, event.getId().value());
    }

    @Test
    @DisplayName("Should create a new PlayerTrainingEvent after successful training and set day to 1 ")
    public void should_create_a_new_PlayerTrainingEvent_with_day_1_after_successful() {
        PlayerTrainingScheduledEvent event =
            PlayerTrainingScheduledEvent.builder()
                .playerId(Player.PlayerId.of("playerId"))
                .id(EventId.generate())
                .skill(PlayerSkill.SCORING)
                .status(PlayerTrainingScheduledEvent.Status.ACTIVE)
                .build();

        when(mockedPlayerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings())
            .thenReturn(List.of(event));
        when(mockedPlayerTrainingEventReadRepository.findLastByPlayerTrainingEvent(event.getId().value()))
            .thenReturn(Optional.of(
                PlayerTrainingEvent.builder()
                    .id(EventId.generate())
                    .pointsAfterTraining(13)
                    .pointsBeforeTraining(11)
                    .scheduledTrainingId(event.getId()
                    .value())
                    .build()
            ));

        processPlayerTrainingUseCase.process();

        verify(mockedExecutePlayerTrainingUseCase, Mockito.times(1))
            .execute(event.getPlayerId(), event.getSkill(), 1, event.getId().value());
    }

    @Test
    @DisplayName("Should create a new PlayerTrainingEvent after not successful training and increase day ")
    public void should_create_a_new_PlayerTrainingEvent_after_not_successful_and_increase_day() {
        final int DAY = 5;
        PlayerTrainingScheduledEvent event =
            PlayerTrainingScheduledEvent.builder()
                .playerId(Player.PlayerId.of("playerId"))
                .id(EventId.generate())
                .skill(PlayerSkill.SCORING)
                .status(PlayerTrainingScheduledEvent.Status.ACTIVE)
                .build();

        when(mockedPlayerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings())
            .thenReturn(List.of(event));
        when(mockedPlayerTrainingEventReadRepository.findLastByPlayerTrainingEvent(event.getId().value()))
            .thenReturn(Optional.of(
                PlayerTrainingEvent.builder()
                    .id(EventId.generate())
                    .pointsAfterTraining(13)
                    .pointsBeforeTraining(13)
                    .currentDay(DAY)
                    .scheduledTrainingId(event.getId()
                        .value())
                    .build()
            ));

        processPlayerTrainingUseCase.process();

        verify(mockedExecutePlayerTrainingUseCase, Mockito.times(1))
            .execute(event.getPlayerId(), event.getSkill(), DAY + 1, event.getId().value());
    }


}