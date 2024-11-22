package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetHistoricalTeamPlayerTrainingUseCaseTest {

    private final PlayerTrainingEventReadRepository playerTrainingEventReadRepository = Mockito.mock(PlayerTrainingEventReadRepository.class);
    private final PlayerReadRepository playerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final GetHistoricalTeamPlayerTrainingUseCase getHistoricalTeamPlayerTrainingUseCase = new GetHistoricalTeamPlayerTrainingUseCase(
        playerTrainingEventReadRepository, playerReadRepository);


    @Test
    @DisplayName("Should return historical team trainings")
    public void should_return_historical_team_trainings() {
        List<Player> testPlayers = TestData.generateTestPlayers(Team.TeamId.of("exampleTeam") , 3);
        when(playerReadRepository.findByTeamId(Team.TeamId.of("exampleTeam"))).thenReturn(testPlayers);

        Instant time = Instant.now();

        when(playerTrainingEventReadRepository.findAllSuccessfulByPlayerIdAndTeamId(testPlayers.get(0).getId(),
            Team.TeamId.of("exampleTeam"))).thenReturn(List.of(
                PlayerTrainingEvent.builder()
                    .teamId(Team.TeamId.of("exampleTeam"))
                    .playerId(testPlayers.get(0).getId())
                    .pointsAfterTraining(35)
                    .skill(PlayerSkill.AERIAL)
                    .currentDay(10)
                    .occurredAt(time)
                    .pointsBeforeTraining(34)
                    .build()));

        Map<String, List<GetHistoricalTeamPlayerTrainingUseCase.PlayerTraining>> testMap =
            getHistoricalTeamPlayerTrainingUseCase.get("exampleTeam");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(time, ZoneId.systemDefault());
        assertEquals(localDateTime.format(formatter), testMap.keySet().iterator().next());
        assertEquals(1 , testMap.get(localDateTime.format(formatter)).size());
        assertEquals(testPlayers.get(0).getId(), testMap.get(localDateTime.format(formatter)).get(0).getPlayer().getId());
    }
}