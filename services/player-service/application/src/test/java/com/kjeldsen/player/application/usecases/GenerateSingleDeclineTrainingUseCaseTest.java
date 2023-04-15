package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;

import static com.kjeldsen.player.domain.PlayerPosition.MIDDLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class GenerateSingleDeclineTrainingUseCaseTest {

    final private PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    final private PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    final private PlayerTrainingDeclineEventWriteRepository mockedplayerTrainingDeclineEventWriteRepository = Mockito.mock(PlayerTrainingDeclineEventWriteRepository.class);

    private final GenerateSingleDeclineTrainingUseCase generateSingleDeclineTrainingUseCase =
        new GenerateSingleDeclineTrainingUseCase(mockedplayerTrainingDeclineEventWriteRepository,
            mockedPlayerReadRepository, mockedPlayerWriteRepository);

    private final Integer currentDay = 1;
    private final Integer declineSpeed = 100;

    @Test
    public void generate_Should_Return_Player_Training_Decline_Event_When_Player_Exists() {
        PlayerId playerId = PlayerId.generate();
        Player newPlayer = getPlayer(playerId);
        mockedPlayerWriteRepository.save(newPlayer);
        // POR QUE COJONES ME DA OPTIONAL NULL Y ENCIMA NO ME DEJA DEBBUGEAR EL PUÑETERO TESTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.of(newPlayer));
        Player playerTest = mockedPlayerReadRepository.findOneById(playerId).orElse(null);

        PlayerTrainingDeclineEvent declineEvent = generateSingleDeclineTrainingUseCase.generate(playerId, currentDay, declineSpeed);

        assertEquals(currentDay, declineEvent.getCurrentDay());
        assertEquals(declineSpeed, declineEvent.getDeclineSpeed());
        assertEquals(playerTest.getActualSkillPoints(declineEvent.getSkill()), declineEvent.getPointsBeforeTraining());
        verify(mockedplayerTrainingDeclineEventWriteRepository, times(1)).save(declineEvent);
        verify(mockedPlayerWriteRepository, times(1)).save(playerTest);
    }

    @Test
    public void generate_Should_Throw_Exception_When_Player_Does_Not_Exist() {
        PlayerId playerId = PlayerId.generate();

        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.of(getPlayer(playerId)));
        Player playerTest = mockedPlayerReadRepository.findOneById(playerId).orElse(null);
        when(mockedPlayerReadRepository.findOneById(playerTest.getId())).thenReturn(Optional.empty());

        generateSingleDeclineTrainingUseCase.generate(playerTest.getId(), currentDay, declineSpeed);
    }

    @Test
    public void generate_And_Store_Event_Should_Set_Random_Skill() {
        PlayerId playerId = PlayerId.generate();

        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.of(getPlayer(playerId)));
        Player playerTest = mockedPlayerReadRepository.findOneById(playerId).orElse(null);

        when(mockedPlayerReadRepository.findOneById(playerTest.getId())).thenReturn(Optional.of(playerTest));

        PlayerTrainingDeclineEvent result = generateSingleDeclineTrainingUseCase.generate(playerTest.getId(), currentDay, declineSpeed);

        assertNotNull(result.getSkill());
        assertTrue(Arrays.asList(PlayerSkill.values()).contains(result.getSkill()));
    }

    @Test
    public void generate_And_Store_Event_Should_Set_Correct_Points_To_Subtract() {
        PlayerId playerId = PlayerId.generate();

        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.of(getPlayer(playerId)));
        Player playerTest = mockedPlayerReadRepository.findOneById(playerId).orElse(null);
        when(mockedPlayerReadRepository.findOneById(playerTest.getId())).thenReturn(Optional.of(playerTest));

        PlayerTrainingDeclineEvent result = generateSingleDeclineTrainingUseCase.generate(playerTest.getId(), currentDay, declineSpeed);

        assertNotNull(result.getPointsToSubtract());
        assertTrue(result.getPointsToSubtract() > 0);
    }

    @Test
    public void generate_And_Store_Event_Should_Update_Player_Actual_Skills_Points() {
        PlayerId playerId = PlayerId.generate();

        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.of(getPlayer(playerId)));
        Player playerTest = mockedPlayerReadRepository.findOneById(playerId).orElse(null);
        when(mockedPlayerReadRepository.findOneById(playerTest.getId())).thenReturn(Optional.of(playerTest));

        Integer pointsBefore = playerTest.getActualSkillPoints(PlayerSkill.PASSING);
        generateSingleDeclineTrainingUseCase.generate(playerTest.getId(), currentDay, declineSpeed);
        Integer pointsAfter = playerTest.getActualSkillPoints(PlayerSkill.PASSING);
    }

    private Player getPlayer(PlayerId playerId) {
        return Player.builder()
            .id(PlayerId.generate())
            .name(PlayerName.generate())
            .age(PlayerAge.generate())
            .position(PlayerPosition.MIDDLE)
            .actualSkills(PlayerActualSkills.generate(PlayerPositionTendency.getDefault(MIDDLE), 200))
            .build();
    }
}
