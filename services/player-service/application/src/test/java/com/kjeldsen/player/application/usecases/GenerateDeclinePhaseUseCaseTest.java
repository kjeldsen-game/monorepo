package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kjeldsen.player.domain.PlayerPosition.MIDDLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GenerateDeclinePhaseUseCaseTest {
    private final PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);

    private final PlayerTrainingDeclineEventWriteRepository mockedPlayerTrainingDeclineEventWriteRepository = Mockito.mock(PlayerTrainingDeclineEventWriteRepository.class);

    private final GenerateDeclinePhaseUseCase generateDeclinePhaseUseCase = new GenerateDeclinePhaseUseCase(mockedPlayerReadRepository, mockedPlayerTrainingDeclineEventWriteRepository);

    @Test
    @DisplayName("Generate should throw if player not found.")
    void generate_should_throw_exception_if_player_not_found() {
        // Arrange
        int declineYears = 1;
        int declineStart = 1;
        Player.PlayerId playerId = new Player.PlayerId("test");

        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> generateDeclinePhaseUseCase.generate(declineYears, declineStart, playerId));
    }

    @Test
    @DisplayName("Generate should call generateAndStoreEventOfDeclinePhase and save to repository.")
    void generate_should_call_generateAndStoreEventOfDeclinePhase_and_save_to_repository() {

        //Arrange
        Integer declineStartAge = 17;
        Integer declineSpeed = 56;

        Player.PlayerId playerId = Player.PlayerId.generate();
        Player playerTest = getPlayer(playerId);

        when(mockedPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.of(playerTest));

        // Act
        generateDeclinePhaseUseCase.generate(declineSpeed, declineStartAge, playerTest.getId());

        // Assert
        ArgumentCaptor<PlayerTrainingDeclineEvent> argumentCaptor = ArgumentCaptor.forClass(PlayerTrainingDeclineEvent.class);
        verify(mockedPlayerTrainingDeclineEventWriteRepository, Mockito.times(1))
            .save(argumentCaptor.capture());

        PlayerTrainingDeclineEvent playerTrainingDeclineEvent = argumentCaptor.getValue();

        assertEquals(playerId, playerTrainingDeclineEvent.getPlayerId());
        assertEquals(declineStartAge, playerTrainingDeclineEvent.getDeclineStartAge());
        assertEquals(declineSpeed, playerTrainingDeclineEvent.getDeclineSpeed());
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
