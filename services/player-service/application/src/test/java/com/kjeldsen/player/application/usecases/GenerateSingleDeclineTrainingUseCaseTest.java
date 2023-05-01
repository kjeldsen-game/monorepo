package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kjeldsen.player.domain.PlayerPosition.MIDDLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class GenerateSingleDeclineTrainingUseCaseTest {

    final private PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    final private PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    final private PlayerTrainingDeclineEventWriteRepository mockedPlayerTrainingDeclineEventWriteRepository = Mockito.mock(PlayerTrainingDeclineEventWriteRepository.class);
    private final GenerateSingleDeclineTrainingUseCase generateSingleDeclineTrainingUseCaseMock =
        new GenerateSingleDeclineTrainingUseCase(mockedPlayerTrainingDeclineEventWriteRepository,
            mockedPlayerReadRepository, mockedPlayerWriteRepository);

    @Test
    @DisplayName("Generate inside GenerateSingleDeclineTrainingUseCase should throw an exception when player does not exist")
    public void generate_Should_Throw_Exception_When_Player_Does_Not_Exist() {
        // Arrange
        Player.PlayerId playerIdMock = mock(Player.PlayerId.class);
        Integer currentDay = 10;
        Integer declineSpeed = 5;
        when(mockedPlayerReadRepository.findOneById(playerIdMock).isEmpty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> generateSingleDeclineTrainingUseCaseMock.generate(playerIdMock, currentDay, declineSpeed));
    }

    @Test
    @DisplayName("Generate should call generateAndStoreEventOfDeclinePhase and save to repository.")
    void generate_should_call_generateAndStoreEventOfSingleDeclinePhase_and_save_to_repository() {

        //Args
        Integer declineSpeed = 50;
        Integer currentDay = 4;

        Player.PlayerId playerId = Player.PlayerId.generate();
        Player playerTest = getPlayer(playerId);

        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.of(playerTest));

        // Act
        generateSingleDeclineTrainingUseCaseMock.generate(playerTest.getId(), currentDay, declineSpeed);

        // Assert
        ArgumentCaptor<PlayerTrainingDeclineEvent> argumentCaptor = ArgumentCaptor.forClass(PlayerTrainingDeclineEvent.class);
        verify(mockedPlayerTrainingDeclineEventWriteRepository, Mockito.times(1))
            .save(argumentCaptor.capture());

        PlayerTrainingDeclineEvent playerTrainingDeclineEvent = argumentCaptor.getValue();

        assertEquals(playerId, playerTrainingDeclineEvent.getPlayerId());
        assertEquals(declineSpeed, playerTrainingDeclineEvent.getDeclineSpeed());

        assertThat(playerTrainingDeclineEvent)
            .matches(player -> player.getPlayerId().equals(playerTest.getId())
                && player.getSkill().equals(any())
                && player.getPointsBeforeTraining().equals(anyInt())
                && player.getPointsAfterTraining().equals(anyInt()));

        verify(mockedPlayerReadRepository, times(1)).findOneById(playerId);
        verify(mockedPlayerTrainingDeclineEventWriteRepository, times(1)).save(any());
    }

    private Player getPlayer(Player.PlayerId playerId) {
        return Player.builder()
            .id(playerId)
            .name(PlayerProvider.name())
            .age(PlayerProvider.age())
            .position(PlayerPosition.MIDDLE)
            .actualSkills(PlayerProvider.skillsBasedOnTendency(PlayerPositionTendency.getDefault(MIDDLE), 200))
            .build();
    }
}
