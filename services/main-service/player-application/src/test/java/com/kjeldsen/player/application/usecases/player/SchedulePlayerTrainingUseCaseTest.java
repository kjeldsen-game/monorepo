package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventWriteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SchedulePlayerTrainingUseCaseTest {


    private static final Player.PlayerId PLAYER_ID = Player.PlayerId.generate();

    @Mock
    private PlayerTrainingScheduledEventWriteRepository playerTrainingScheduledEventWriteRepository;
    @Mock
    private PlayerTrainingScheduledEventReadRepository playerTrainingScheduledEventReadRepository;
    @Mock
    private PlayerReadRepository playerReadRepository;
    @InjectMocks
    private SchedulePlayerTrainingUseCase schedulePlayerTrainingUseCase;


    @Test
    void should_schedule_a_new_player_training_scheduled_events() {
        Player player = Player.builder().id(PLAYER_ID).build();
        Mockito.when(playerReadRepository.findOneById(PLAYER_ID)).thenReturn(Optional.of(player));

        List<PlayerTrainingScheduledEvent> testList = List.of(
            PlayerTrainingScheduledEvent.builder().playerId(PLAYER_ID).skill(PlayerSkill.OFFENSIVE_POSITIONING).build(),
            PlayerTrainingScheduledEvent.builder().playerId(PLAYER_ID).skill(PlayerSkill.DEFENSIVE_POSITIONING).build());

        Mockito.when(playerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings()).thenReturn(testList);
        schedulePlayerTrainingUseCase.schedule(PLAYER_ID, List.of(PlayerSkill.BALL_CONTROL, PlayerSkill.SCORING));

        assertEquals(PlayerTrainingScheduledEvent.Status.INACTIVE, testList.get(0).getStatus());
        assertEquals(PlayerTrainingScheduledEvent.Status.INACTIVE, testList.get(0).getStatus());
        verify(playerTrainingScheduledEventWriteRepository, times(4)).save(any());
    }

    @Test
    void should_not_create_new_player_training_scheduled_events_when_same_skills_are_passed() {
        Player player = Player.builder().id(PLAYER_ID).build();
        Mockito.when(playerReadRepository.findOneById(PLAYER_ID)).thenReturn(Optional.of(player));

        List<PlayerTrainingScheduledEvent> testList = List.of(
            PlayerTrainingScheduledEvent.builder().playerId(PLAYER_ID).skill(PlayerSkill.SCORING)
                .status(PlayerTrainingScheduledEvent.Status.ACTIVE).build(),
            PlayerTrainingScheduledEvent.builder().playerId(PLAYER_ID).skill(PlayerSkill.DEFENSIVE_POSITIONING).build());

        Mockito.when(playerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings()).thenReturn(testList);
        schedulePlayerTrainingUseCase.schedule(PLAYER_ID, List.of(PlayerSkill.BALL_CONTROL, PlayerSkill.SCORING));


        assertEquals(PlayerTrainingScheduledEvent.Status.ACTIVE, testList.get(0).getStatus());
        assertEquals(PlayerTrainingScheduledEvent.Status.INACTIVE, testList.get(1).getStatus());
        // Call only for the BALL_CONTROL skill as SCORING is already there
        verify(playerTrainingScheduledEventWriteRepository, times(2)).save(any());
    }
}