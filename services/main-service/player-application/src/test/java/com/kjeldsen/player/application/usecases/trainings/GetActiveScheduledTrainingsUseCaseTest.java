package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingScheduledEventReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetActiveScheduledTrainingsUseCaseTest {

    private final PlayerReadRepository playerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final PlayerTrainingScheduledEventReadRepository playerTrainingScheduledEventReadRepository =
        Mockito.mock(PlayerTrainingScheduledEventReadRepository.class);
    private final GetActiveScheduledTrainingsUseCase getActiveScheduledTrainingsUseCase = new GetActiveScheduledTrainingsUseCase(
        playerReadRepository, playerTrainingScheduledEventReadRepository);

    @Test
    @DisplayName("Should return all scheduled trainings for team with playerData")
    public void should_return_all_scheduled_trainings_for_team_with_playerData() {
        Team.TeamId teamId = TestData.generateTestTeamId();
        List<Player> players = TestData.generateTestPlayers(teamId, 3);
        when(playerReadRepository.findByTeamId(teamId)).thenReturn(players);
        List<PlayerTrainingScheduledEvent> testEvents = List.of(
            PlayerTrainingScheduledEvent.builder().playerId(players.get(0).getId())
                .skill(PlayerSkill.SCORING).occurredAt(Instant.now()).build(),
            PlayerTrainingScheduledEvent.builder().playerId(players.get(0).getId())
                .skill(PlayerSkill.BALL_CONTROL).occurredAt(Instant.now().minus(5, ChronoUnit.MINUTES)).build(),
            PlayerTrainingScheduledEvent.builder().playerId(players.get(1).getId())
                .occurredAt(Instant.now())
                .skill(PlayerSkill.AERIAL).build()
        );
        when(playerTrainingScheduledEventReadRepository.findAllActiveScheduledTrainings()).thenReturn(testEvents);

        List<GetActiveScheduledTrainingsUseCase.PlayerScheduledTraining> result =
            getActiveScheduledTrainingsUseCase.get(teamId);

        assertFalse(result.isEmpty());
        assertEquals(3, result.size());
        assertTrue(result.get(2).getSkills().isEmpty());
        assertEquals(2, result.get(0).getSkills().size());
        assertEquals(1, result.get(1).getSkills().size());
        assertEquals(PlayerSkill.BALL_CONTROL, result.get(0).getSkills().get(0));
    }
}