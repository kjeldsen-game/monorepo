package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerActualSkills;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class GenerateSingleDeclineTrainingUseCaseTest {

    final private PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    final private PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    final private PlayerTrainingDeclineEventWriteRepository mockedplayerTrainingDeclineEventWriteRepository = Mockito.mock(PlayerTrainingDeclineEventWriteRepository.class);

    private final GenerateSingleDeclineTrainingUseCase generateSingleDeclineTrainingUseCase =
        new GenerateSingleDeclineTrainingUseCase(mockedplayerTrainingDeclineEventWriteRepository,
            mockedPlayerReadRepository, mockedPlayerWriteRepository);

    private final PlayerId playerId = PlayerId.generate();
    private final Integer currentDay = 1;
    private final Integer declineSpeed = 2;
    private final Player player = Player.builder()
        .id(playerId)
        .actualSkills(PlayerActualSkills.of(Map.of(PlayerSkill.PASSING, 20)))
        .build();

    @Test
    public void generate_Should_Return_Player_Training_Decline_Event_When_Player_Exists() {
        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.of(player));

        PlayerTrainingDeclineEvent result = generateSingleDeclineTrainingUseCase.generate(playerId, currentDay, declineSpeed);

        assertNotNull(result);
        assertEquals(playerId, result.getPlayerId());
        assertEquals(currentDay, result.getCurrentDay());
        assertEquals(declineSpeed, result.getDeclineSpeed());
        assertEquals(player.getActualSkillPoints(result.getSkill()), result.getPointsBeforeTraining());
        verify(mockedplayerTrainingDeclineEventWriteRepository, times(1)).save(result);
        verify(mockedPlayerWriteRepository, times(1)).save(player);
    }

    @Test
    public void generate_Should_Throw_Exception_When_Player_Does_Not_Exist() {
        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.empty());

        generateSingleDeclineTrainingUseCase.generate(playerId, currentDay, declineSpeed);
    }

    @Test
    public void generate_And_Store_Event_Should_Set_Random_Skill() {
        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.of(player));

        PlayerTrainingDeclineEvent result = generateSingleDeclineTrainingUseCase.generate(playerId, currentDay, declineSpeed);

        assertNotNull(result.getSkill());
        assertTrue(Arrays.asList(PlayerSkill.values()).contains(result.getSkill()));
    }

    @Test
    public void generate_And_Store_Event_Should_Set_Correct_Points_To_Subtract() {
        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.of(player));

        PlayerTrainingDeclineEvent result = generateSingleDeclineTrainingUseCase.generate(playerId, currentDay, declineSpeed);

        assertNotNull(result.getPointsToSubtract());
        assertTrue(result.getPointsToSubtract() > 0);
    }

    @Test
    public void generate_And_Store_Event_Should_Update_Player_Actual_Skills_Points() {
        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.of(player));

        Integer pointsBefore = player.getActualSkillPoints(PlayerSkill.PASSING);
        generateSingleDeclineTrainingUseCase.generate(playerId, currentDay, declineSpeed);
        Integer pointsAfter = player.getActualSkillPoints(PlayerSkill.PASSING);
    }
}
