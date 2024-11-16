package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.generator.DeclinePointsGenerator;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ProcessDeclineTrainingUseCaseTest {

    private static final Integer DECLINE_AGE_TRIGGER = 27;
    private final PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.
        class);
    private final PlayerTrainingDeclineEventReadRepository mockedPlayerTrainingDeclineEventReadRepository =
        Mockito.mock(PlayerTrainingDeclineEventReadRepository.class);
    private final PlayerTrainingDeclineEventWriteRepository mockedPlayerTrainingDeclineEventWriteRepository =
        Mockito.mock(PlayerTrainingDeclineEventWriteRepository.class);
    ProcessDeclineTrainingUseCase processDeclineTrainingUseCase = new ProcessDeclineTrainingUseCase(mockedPlayerReadRepository,
        mockedPlayerWriteRepository, mockedPlayerTrainingDeclineEventReadRepository, mockedPlayerTrainingDeclineEventWriteRepository);

    @Test
    @DisplayName("Should throw an error if player age is invalid")
    public void should_throw_an_error_if_player_age_is_invalid() {
        List<Player> players = TestData.generateTestPlayers(Team.TeamId.of("teamID"), 1);
        players.get(0).setAge(PlayerAge.builder().days(12.0).months(5.0).years(15).build());

        when(mockedPlayerReadRepository.findPlayerOverAge(DECLINE_AGE_TRIGGER))
            .thenReturn(players);

        assertEquals("The age of the player must be greater or equal than 27 years.", assertThrows(
            IllegalArgumentException.class, processDeclineTrainingUseCase::process).getMessage());
    }

    @Test
    @DisplayName("Should process and decrease training")
    public void should_process_and_decrease_training() {
        List<Player> players = TestData.generateTestPlayers(Team.TeamId.of("teamID"), 2);
        players.get(0).setAge(PlayerAge.builder().days(12.0).months(5.0).years(30).build());
        players.get(0).setActualSkills(Map.of(PlayerSkill.AERIAL, PlayerSkills.builder().actual(34).potential(35).build()));
        players.get(1).setAge(PlayerAge.builder().days(12.0).months(5.0).years(30).build());
        players.get(1).setActualSkills(Map.of(PlayerSkill.AERIAL, PlayerSkills.builder().actual(29).potential(35).build()));

        when(mockedPlayerReadRepository.findPlayerOverAge(DECLINE_AGE_TRIGGER))
            .thenReturn(players);

        when(mockedPlayerTrainingDeclineEventReadRepository.findLatestByPlayerId(players.get(0).getId())).thenReturn(
            Optional.ofNullable(PlayerTrainingDeclineEvent.builder()
                .playerId(players.get(0).getId())
                .pointsAfterTraining(21)
                .pointsBeforeTraining(22)
                .build()));

        try (
            MockedStatic<PlayerProvider> playerProviderMockedStatic = Mockito.mockStatic(PlayerProvider.class);
            MockedStatic<DeclinePointsGenerator> declinePointsGeneratorMockedStatic = Mockito.mockStatic(DeclinePointsGenerator.class);
        ) {
            playerProviderMockedStatic.when(() -> PlayerProvider.randomSkillForSpecificPlayerDeclineUseCase(Optional.of(players.get(0))))
                .thenReturn(PlayerSkill.AERIAL);
            declinePointsGeneratorMockedStatic.when(() -> DeclinePointsGenerator.generateDeclinePoints(1, 4))
                .thenReturn(1);
            processDeclineTrainingUseCase.process();
        }

        assertEquals(33, players.get(0).getActualSkillPoints(PlayerSkill.AERIAL));
        verify(mockedPlayerReadRepository, times(1)).findPlayerOverAge(anyInt());
        verify(mockedPlayerWriteRepository, times(1)).save(any());
        verify(mockedPlayerTrainingDeclineEventReadRepository, times(1)).findLatestByPlayerId(any());
        verify(mockedPlayerTrainingDeclineEventWriteRepository, times(1)).save(any(PlayerTrainingDeclineEvent.class));
    }
}