package com.kjeldsen.player.application.usecases.trainings.player;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.training.PlayerTrainingScheduledEventReadRepository;
import com.kjeldsen.player.domain.repositories.training.PlayerTrainingScheduledEventWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SchedulePlayerTrainingUseCaseTest {

    private final PlayerTrainingScheduledEventWriteRepository mockedPlayerTrainingScheduledEventWriteRepository =
        Mockito.mock(PlayerTrainingScheduledEventWriteRepository.class);
    private final PlayerTrainingScheduledEventReadRepository mockedPlayerTrainingScheduledEventReadRepository =
        Mockito.mock(PlayerTrainingScheduledEventReadRepository.class);
    private final PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final SchedulePlayerTrainingUseCase schedulePlayerTrainingUseCase = new SchedulePlayerTrainingUseCase(
        mockedPlayerTrainingScheduledEventWriteRepository, mockedPlayerTrainingScheduledEventReadRepository ,mockedPlayerReadRepository
    );

    @Test
    @DisplayName("Should throw error when player is null")
    public void should_throw_error_when_player_is_null() {
        Player.PlayerId playerId = Player.PlayerId.generate();

        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.empty());

        assertEquals("Player not found.",
            assertThrows(RuntimeException.class,
                () -> schedulePlayerTrainingUseCase.schedule(playerId, PlayerSkill.AERIAL)
            ).getMessage());
    }

    @Test
    @DisplayName("Should throw error when skill is already assigned to active training")
    public void should_throw_error_when_skill_is_already_assigned_to_active_training() {
        Player player = TestData.generateTestPlayers(Team.TeamId.generate(), 1).get(0);
        player.setActualSkills(Map.of(PlayerSkill.AERIAL, PlayerSkills.builder().actual(12).potential(20).build()));
        when(mockedPlayerReadRepository.findOneById(player.getId())).thenReturn(Optional.of(player));

        PlayerTrainingScheduledEvent event = PlayerTrainingScheduledEvent.builder()
            .skill(PlayerSkill.AERIAL).playerId(player.getId()).build();

        when(mockedPlayerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings()).thenReturn(
            List.of(event));

        assertEquals("Training for the skill is already scheduled.",
            assertThrows(RuntimeException.class,
                () -> schedulePlayerTrainingUseCase.schedule(player.getId(), PlayerSkill.AERIAL)
            ).getMessage());
    }

    @Test
    @DisplayName("Should schedule a training when its not assigned and count of active trainings is less than 2")
    public void should_schedule_training_when_its_not_assigned_and_count_of_active_trainings() {
        Player player = TestData.generateTestPlayers(Team.TeamId.generate(), 1).get(0);
        when(mockedPlayerReadRepository.findOneById(player.getId())).thenReturn(Optional.of(player));
        ArgumentCaptor<PlayerTrainingScheduledEvent> eventCaptor = ArgumentCaptor.forClass(PlayerTrainingScheduledEvent.class);

        PlayerTrainingScheduledEvent event = PlayerTrainingScheduledEvent.builder()
            .skill(PlayerSkill.AERIAL).playerId(player.getId()).build();

        when(mockedPlayerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings()).thenReturn(
            List.of(event));

        schedulePlayerTrainingUseCase.schedule(player.getId(), PlayerSkill.SCORING);
        verify(mockedPlayerTrainingScheduledEventWriteRepository, times(1)).save(eventCaptor.capture());

        assertEquals(player.getId(), eventCaptor.getValue().getPlayerId());
        assertEquals(PlayerSkill.SCORING, eventCaptor.getValue().getSkill());
        assertEquals(PlayerTrainingScheduledEvent.Status.ACTIVE, eventCaptor.getValue().getStatus());

    }

    @Test
    @DisplayName("Should schedule a training and remove the older one when 2 assigned already")
    public void should_schedule_and_remove_older_one() {
        Player player = TestData.generateTestPlayers(Team.TeamId.generate(), 1).get(0);
        when(mockedPlayerReadRepository.findOneById(player.getId())).thenReturn(Optional.of(player));
        ArgumentCaptor<PlayerTrainingScheduledEvent> eventCaptor = ArgumentCaptor.forClass(PlayerTrainingScheduledEvent.class);

        PlayerTrainingScheduledEvent event1 = PlayerTrainingScheduledEvent.builder()
            .skill(PlayerSkill.AERIAL).playerId(player.getId()).occurredAt(Instant.now()
            .minus(10, ChronoUnit.DAYS)).build();

        PlayerTrainingScheduledEvent event2 = PlayerTrainingScheduledEvent.builder()
            .skill(PlayerSkill.OFFENSIVE_POSITIONING).occurredAt(Instant.now()
            .minus(20, ChronoUnit.DAYS)).playerId(player.getId()).build();

        when(mockedPlayerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings()).thenReturn(
            List.of(event1, event2));

        schedulePlayerTrainingUseCase.schedule(player.getId(), PlayerSkill.SCORING);
        verify(mockedPlayerTrainingScheduledEventWriteRepository, times(2)).save(eventCaptor.capture());

        List<PlayerTrainingScheduledEvent> capturedEvents = eventCaptor.getAllValues();

        assertEquals(PlayerTrainingScheduledEvent.Status.INACTIVE, capturedEvents.get(0).getStatus());

        assertEquals(player.getId(), capturedEvents.get(1).getPlayerId());
        assertEquals(PlayerSkill.SCORING, capturedEvents.get(1).getSkill());
        assertEquals(PlayerTrainingScheduledEvent.Status.ACTIVE, capturedEvents.get(1).getStatus());

    }

}