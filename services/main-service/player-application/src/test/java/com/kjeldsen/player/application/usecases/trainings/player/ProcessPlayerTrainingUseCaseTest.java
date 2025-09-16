package com.kjeldsen.player.application.usecases.trainings.player;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.domain.repositories.training.PlayerTrainingScheduledEventReadRepository;
import com.kjeldsen.player.domain.repositories.training.PlayerTrainingScheduledEventWriteRepository;
import com.kjeldsen.player.domain.repositories.training.TrainingEventReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessPlayerTrainingUseCaseTest {

    private final PlayerTrainingScheduledEventReadRepository mockedPlayerTrainingScheduledEventReadRepository = Mockito.mock(PlayerTrainingScheduledEventReadRepository.class);
    private final PlayerTrainingScheduledEventWriteRepository mockedPlayerTrainingScheduledEventWriteRepository = Mockito.mock(PlayerTrainingScheduledEventWriteRepository.class);
    private final ExecutePlayerTrainingUseCase mockedExecutePlayerTrainingUseCase = Mockito.mock(ExecutePlayerTrainingUseCase.class);

    @Mock
    private TrainingEventReadRepository mockedTrainingReadWriteRepository;

    @InjectMocks
    private final ProcessPlayerTrainingUseCase processPlayerTrainingUseCase = new ProcessPlayerTrainingUseCase(
        mockedPlayerTrainingScheduledEventReadRepository, mockedPlayerTrainingScheduledEventWriteRepository, mockedExecutePlayerTrainingUseCase
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

        when(mockedTrainingReadWriteRepository.findFirstByReferenceOrderByOccurredAtDesc(event.getId().value()))
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

        when(mockedTrainingReadWriteRepository.findFirstByReferenceOrderByOccurredAtDesc(event.getId().value()))
            .thenReturn(Optional.of(
                TrainingEvent.builder()
                    .skill(PlayerSkill.SCORING)
                    .player(Player.builder().id(Player.PlayerId.of("playerId")).build())
                    .pointsAfterTraining(13)
                    .pointsBeforeTraining(11)
                    .reference(event.getId()
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
        when(mockedTrainingReadWriteRepository.findFirstByReferenceOrderByOccurredAtDesc(event.getId().value()))
            .thenReturn(Optional.of(
                TrainingEvent.builder()
                    .player(Player.builder().id(Player.PlayerId.of("playerId")).build())
                    .skill(PlayerSkill.SCORING)
                    .pointsAfterTraining(13)
                    .pointsBeforeTraining(13)
                    .currentDay(DAY)
                    .reference(event.getId().value())
                    .build()
            ));

        processPlayerTrainingUseCase.process();

        verify(mockedExecutePlayerTrainingUseCase, Mockito.times(1))
            .execute(event.getPlayerId(), event.getSkill(), DAY + 1, event.getId().value());
    }
}