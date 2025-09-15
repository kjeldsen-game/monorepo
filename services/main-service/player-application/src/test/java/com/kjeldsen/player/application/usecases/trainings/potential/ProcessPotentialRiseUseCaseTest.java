package com.kjeldsen.player.application.usecases.trainings.potential;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.application.usecases.trainings.GetTrainingEventsUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.generator.PotentialRiseGenerator;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.repositories.training.TrainingEventReadRepository;
import com.kjeldsen.player.domain.repositories.training.TrainingEventWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessPotentialRiseUseCaseTest {

    private static final Integer MAX_AGE = 21;
    private final PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);

    @InjectMocks
    private final ProcessPotentialRiseUseCase potentialRiseUseCase = new ProcessPotentialRiseUseCase(
        mockedPlayerReadRepository, mockedPlayerWriteRepository
    );

    @Mock
    private TrainingEventWriteRepository mockedTrainingEventWriteRepository;


    @Test
    @DisplayName("Should throw error if player age is over")
    public void should_throw_error_if_player_Age_is_over(){
        List<Player> players = TestData.generateTestPlayers(Team.TeamId.of("testTeam"), 5);
        players.get(3).getAge().setYears(22);

        when(mockedPlayerReadRepository.findPlayerUnderAge(MAX_AGE)).thenReturn(players);

        assertEquals("The age of the player must be less than 21 years.", assertThrows(
            IllegalArgumentException.class, potentialRiseUseCase::process).getMessage());
    }

    @Test
    @DisplayName("Should update the potential because rise happened")
    public void should_update_the_potential_because_rise_happened(){
        List<Player> players = TestData.generateTestPlayers(Team.TeamId.of("testTeam"), 1);
        players.get(0).getAge().setYears(19);
        players.get(0).setActualSkills(Map.of(PlayerSkill.AERIAL, PlayerSkills.builder().actual(12).potential(13).build()));

        when(mockedPlayerReadRepository.findPlayerUnderAge(MAX_AGE)).thenReturn(players);
        try (
            MockedStatic<PotentialRiseGenerator> potentialStaticMock = Mockito.mockStatic(PotentialRiseGenerator.class);
            MockedStatic<PlayerProvider> playerStaticMock = Mockito.mockStatic(PlayerProvider.class);
        )
        {
            potentialStaticMock.when(PotentialRiseGenerator::generatePotentialRaise).thenReturn(1);
            playerStaticMock.when(() -> PlayerProvider.randomSkillForSpecificPlayer(players.get(0))).thenReturn(PlayerSkill.AERIAL);
            potentialRiseUseCase.process();
        }

        assertEquals(14, players.get(0).getActualSkills().get(PlayerSkill.AERIAL).getPotential());
        verify(mockedPlayerReadRepository, times(1)).findPlayerUnderAge(MAX_AGE);
        verify(mockedPlayerWriteRepository, times(1)).save(any(Player.class));

        ArgumentCaptor<TrainingEvent> captor = ArgumentCaptor.forClass(TrainingEvent.class);
        verify(mockedTrainingEventWriteRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getType()).isEqualTo(TrainingEvent.TrainingType.POTENTIAL_RISE);
    }

    @Test
    @DisplayName("Should not update the potential because rise did not happen")
    public void should_not_update_potential_because_rise_didnt_happened(){
        List<Player> players = TestData.generateTestPlayers(Team.TeamId.of("testTeam"), 1);
        players.get(0).getAge().setYears(19);
        players.get(0).setActualSkills(Map.of(PlayerSkill.AERIAL, PlayerSkills.builder().actual(12).potential(13).build()));

        when(mockedPlayerReadRepository.findPlayerUnderAge(MAX_AGE)).thenReturn(players);
        try (
            MockedStatic<PotentialRiseGenerator> potentialStaticMock = Mockito.mockStatic(PotentialRiseGenerator.class);
            MockedStatic<PlayerProvider> playerStaticMock = Mockito.mockStatic(PlayerProvider.class);
        )
        {
            potentialStaticMock.when(PotentialRiseGenerator::generatePotentialRaise).thenReturn(0);
            playerStaticMock.when(() -> PlayerProvider.randomSkillForSpecificPlayer(players.get(0))).thenReturn(PlayerSkill.AERIAL);
            potentialRiseUseCase.process();
        }

        assertEquals(13, players.get(0).getActualSkills().get(PlayerSkill.AERIAL).getPotential());
        verify(mockedPlayerReadRepository, times(1)).findPlayerUnderAge(MAX_AGE);
        verify(mockedPlayerWriteRepository, times(0)).save(any(Player.class));
        verify(mockedTrainingEventWriteRepository, times(0)).save(any(TrainingEvent.class));
    }


}