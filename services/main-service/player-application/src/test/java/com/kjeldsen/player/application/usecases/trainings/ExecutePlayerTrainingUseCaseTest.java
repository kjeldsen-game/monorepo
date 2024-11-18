package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.generator.PointsGenerator;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExecutePlayerTrainingUseCaseTest {

    private static final int DAY = 1;
    private final PlayerTrainingEventWriteRepository mockedPlayerTrainingEventWriteRepository =
        Mockito.mock(PlayerTrainingEventWriteRepository.class);
    private final PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    private final ExecutePlayerTrainingUseCase executePlayerTrainingUseCase = new ExecutePlayerTrainingUseCase(
        mockedPlayerTrainingEventWriteRepository, mockedPlayerReadRepository, mockedPlayerWriteRepository
    );

    @Test
    @DisplayName("Should throw error when player is null")
    public void should_throw_error_when_player_is_null() {
        Player.PlayerId playerId = Player.PlayerId.generate();

        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.empty());

        assertEquals("Player not found.",
            assertThrows(RuntimeException.class,
                () -> executePlayerTrainingUseCase.execute(playerId, PlayerSkill.AERIAL, 3, "eventId")
            ).getMessage());
    }

    @Test
    @DisplayName("Should update the points and bloom is active")
    public void should_update_points_by_double_when_bloom_active() {
        Player player = TestData.generateTestPlayers(Team.TeamId.generate(), 1).get(0);
        player.setBloomYear(21);
        player.getAge().setYears(21);
        player.setActualSkills(Map.of(PlayerSkill.AERIAL, PlayerSkills.builder().actual(12).potential(20).build()));
        when(mockedPlayerReadRepository.findOneById(player.getId())).thenReturn(Optional.of(player));

        try (
            MockedStatic<PointsGenerator> mockedPointsGenerator = Mockito.mockStatic(PointsGenerator.class);
            )
        {
            mockedPointsGenerator.when(() -> PointsGenerator.generatePointsRise(DAY)).thenReturn(2);
            executePlayerTrainingUseCase.execute(player.getId(), PlayerSkill.AERIAL, DAY, "eventId");
        }

        assertEquals(16, player.getActualSkills().get(PlayerSkill.AERIAL).getActual());
        verify(mockedPlayerWriteRepository, times(1)).save(any(Player.class));
        verify(mockedPlayerTrainingEventWriteRepository, times(1)).save(any(PlayerTrainingEvent.class));
    }

    @Test
    @DisplayName("Should update the potential points and actual by 1 because its maxed")
    public void should_update_potential_points_and_actual_by_one_because_its_maxed() {
        Player player = TestData.generateTestPlayers(Team.TeamId.generate(), 1).get(0);
        player.setBloomYear(21);
        player.getAge().setYears(21);
        player.setActualSkills(Map.of(PlayerSkill.AERIAL, PlayerSkills.builder().actual(12).potential(12).build()));
        when(mockedPlayerReadRepository.findOneById(player.getId())).thenReturn(Optional.of(player));

        try (
            MockedStatic<PointsGenerator> mockedPointsGenerator = Mockito.mockStatic(PointsGenerator.class);
        )
        {
            mockedPointsGenerator.when(() -> PointsGenerator.generatePointsRise(DAY)).thenReturn(2);
            executePlayerTrainingUseCase.execute(player.getId(), PlayerSkill.AERIAL, DAY, "eventId");
        }

        assertEquals(13, player.getActualSkills().get(PlayerSkill.AERIAL).getActual());
        assertEquals(13, player.getActualSkills().get(PlayerSkill.AERIAL).getPotential());
        verify(mockedPlayerWriteRepository, times(1)).save(any(Player.class));
        verify(mockedPlayerTrainingEventWriteRepository, times(1)).save(any(PlayerTrainingEvent.class));
    }

    @Test
    @DisplayName("Should update actual but not exceed potential")
    public void should_update_actual_but_not_exceed_potential() {
        Player player = TestData.generateTestPlayers(Team.TeamId.generate(), 1).get(0);
        player.setBloomYear(21);
        player.getAge().setYears(21);
        player.setActualSkills(Map.of(PlayerSkill.AERIAL, PlayerSkills.builder().actual(12).potential(14).build()));
        when(mockedPlayerReadRepository.findOneById(player.getId())).thenReturn(Optional.of(player));

        try (
            MockedStatic<PointsGenerator> mockedPointsGenerator = Mockito.mockStatic(PointsGenerator.class);
        )
        {
            mockedPointsGenerator.when(() -> PointsGenerator.generatePointsRise(DAY)).thenReturn(2);
            executePlayerTrainingUseCase.execute(player.getId(), PlayerSkill.AERIAL, DAY, "eventId");
        }

        assertEquals(14, player.getActualSkills().get(PlayerSkill.AERIAL).getActual());
        assertEquals(14, player.getActualSkills().get(PlayerSkill.AERIAL).getPotential());
        verify(mockedPlayerWriteRepository, times(1)).save(any(Player.class));
        verify(mockedPlayerTrainingEventWriteRepository, times(1)).save(any(PlayerTrainingEvent.class));
    }

    @Test
    @DisplayName("Should not update the points when training is not successful")
    public void should_not_update_points_when_training_is_not_successful() {
        Player player = TestData.generateTestPlayers(Team.TeamId.generate(), 1).get(0);
        player.setBloomYear(21);
        player.getAge().setYears(21);
        player.setActualSkills(Map.of(PlayerSkill.AERIAL, PlayerSkills.builder().actual(12).potential(20).build()));
        when(mockedPlayerReadRepository.findOneById(player.getId())).thenReturn(Optional.of(player));
        ArgumentCaptor<PlayerTrainingEvent> eventCaptor = ArgumentCaptor.forClass(PlayerTrainingEvent.class);

        try (
            MockedStatic<PointsGenerator> mockedPointsGenerator = Mockito.mockStatic(PointsGenerator.class);
        )
        {
            mockedPointsGenerator.when(() -> PointsGenerator.generatePointsRise(DAY)).thenReturn(0);
            executePlayerTrainingUseCase.execute(player.getId(), PlayerSkill.AERIAL, DAY, "eventId");
        }
        verify(mockedPlayerTrainingEventWriteRepository, times(1)).save(eventCaptor.capture());
        PlayerTrainingEvent capturedEvent = eventCaptor.getValue();

        assertNotNull(capturedEvent);
        assertEquals(PlayerSkill.AERIAL, capturedEvent.getSkill());

        assertEquals(12, player.getActualSkills().get(PlayerSkill.AERIAL).getActual());
        verify(mockedPlayerWriteRepository, times(1)).save(any(Player.class));
    }

}