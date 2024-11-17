package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.application.usecases.trainings.ExecutePlayerTrainingUseCase;
import com.kjeldsen.player.application.usecases.trainings.ProcessPlayerTrainingUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessPlayerTrainingUseCaseTest {

    @Mock
    private PlayerTrainingScheduledEventReadRepository mockedPlayerTrainingScheduledEventReadRepository;
    @Mock
    private PlayerTrainingEventReadRepository mockedPlayerTrainingEventReadRepository;
    @Mock
    private ExecutePlayerTrainingUseCase mockedExecutePlayerTrainingUseCase;
    @InjectMocks
    private ProcessPlayerTrainingUseCase processPlayerTrainingUseCase;

    @Test
    @DisplayName("Should process PlayerTrainings when event is present")
    public void should_process_player_trainings_when_event_is_present() {
        List<PlayerTrainingScheduledEvent> testEvents = List.of(
            PlayerTrainingScheduledEvent.builder().id(EventId.from("eventId")).playerId(Player.PlayerId.of("exampleId"))
                .status(PlayerTrainingScheduledEvent.Status.ACTIVE)
                .skill(PlayerSkill.SCORING).build()
            );
        PlayerTrainingEvent event = PlayerTrainingEvent.builder()
            .pointsBeforeTraining(22).pointsAfterTraining(24).points(2)
            .skill(PlayerSkill.SCORING).scheduledTrainingId("eventId").build();

        when(mockedPlayerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings()).thenReturn(
            testEvents);
        when(mockedPlayerTrainingEventReadRepository.findLastByPlayerTrainingEvent("eventId")).thenReturn(
            Optional.of(event));

        processPlayerTrainingUseCase.process();


        verify(mockedExecutePlayerTrainingUseCase, times(1)).execute(Player.PlayerId.of("exampleId"),
            PlayerSkill.SCORING, 1, "eventId");
    }

    @Test
    @DisplayName("Should process PlayerTrainings when training was not successful")
    public void should_process_player_trainings_when_events_missing() {
        int currentDay = 6;
        List<PlayerTrainingScheduledEvent> testEvents = List.of(
            PlayerTrainingScheduledEvent.builder().id(EventId.from("eventId")).playerId(Player.PlayerId.of("exampleId"))
                .status(PlayerTrainingScheduledEvent.Status.ACTIVE)
                .skill(PlayerSkill.SCORING).build()
        );
        PlayerTrainingEvent event = PlayerTrainingEvent.builder()
            .pointsBeforeTraining(22).pointsAfterTraining(22).points(0).currentDay(currentDay)
            .skill(PlayerSkill.SCORING).scheduledTrainingId("eventId").build();

        when(mockedPlayerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings()).thenReturn(
            testEvents);
        when(mockedPlayerTrainingEventReadRepository.findLastByPlayerTrainingEvent("eventId")).thenReturn(
            Optional.of(event));

        processPlayerTrainingUseCase.process();

        verify(mockedExecutePlayerTrainingUseCase, times(1)).execute(Player.PlayerId.of("exampleId"),
            PlayerSkill.SCORING, currentDay + 1, "eventId");
    }

    @Test
    @DisplayName("Should process PlayerTrainings")
    public void should_process_multiple_trainings() {
        List<PlayerTrainingScheduledEvent> testEvents = List.of(
            PlayerTrainingScheduledEvent.builder().id(EventId.from("eventId1")).playerId(Player.PlayerId.of("exampleId"))
                .status(PlayerTrainingScheduledEvent.Status.ACTIVE)
                .skill(PlayerSkill.SCORING).build(),
            PlayerTrainingScheduledEvent.builder().id(EventId.from("eventId2")).playerId(Player.PlayerId.of("exampleId"))
                .status(PlayerTrainingScheduledEvent.Status.ACTIVE)
                .skill(PlayerSkill.AERIAL).build()
            );
        PlayerTrainingEvent eventScoring = PlayerTrainingEvent.builder()
            .pointsBeforeTraining(22).pointsAfterTraining(22).points(0).currentDay(2)
            .skill(PlayerSkill.SCORING).scheduledTrainingId("eventId1").build();
        PlayerTrainingEvent eventAerial = PlayerTrainingEvent.builder()
            .pointsBeforeTraining(22).pointsAfterTraining(23).points(1).currentDay(4)
            .skill(PlayerSkill.AERIAL).scheduledTrainingId("eventId2").build();

        when(mockedPlayerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings()).thenReturn(
            testEvents);
        when(mockedPlayerTrainingEventReadRepository.findLastByPlayerTrainingEvent(any())).thenReturn(Optional.of(eventScoring),
            Optional.of(eventAerial));

        processPlayerTrainingUseCase.process();
        verify(mockedExecutePlayerTrainingUseCase).execute(Player.PlayerId.of("exampleId"), PlayerSkill.SCORING,3, "eventId1");
        verify(mockedExecutePlayerTrainingUseCase).execute(Player.PlayerId.of("exampleId"), PlayerSkill.AERIAL,1, "eventId2");
    }
}