package com.kjeldsen.player.application.usecases;

import com.kjeldsen.events.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class GenerateSingleDeclineTrainingUseCaseTest {
    private static final Integer DECLINE_SPEED = 50;
    private static final Integer CURRENT_DAY = 4;
    private static final Player.PlayerId PLAYER_ID = mock(Player.PlayerId.class);
    private static final EventId EVENT_ID = EventId.from("event-id");
    private static final Instant NOW = Instant.now();
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
        when(mockedPlayerReadRepository.findOneById(null).isEmpty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> generateSingleDeclineTrainingUseCaseMock.generate(PLAYER_ID, CURRENT_DAY, DECLINE_SPEED));
        assertEquals("Player not found.", exception.getMessage());
    }

    /*
    @Test
    @DisplayName("Generate should call generateAndStoreEventOfDeclinePhase and save to repository.")
    void generate_should_call_generateAndStoreEventOfSingleDeclinePhase_and_save_to_repository() {

        Player playerMock = mock(Player.class);
        PlayerTrainingDeclineEvent playerTrainingDeclineEventMock = Mockito.mock(PlayerTrainingDeclineEvent.class);

        when(mockedPlayerReadRepository.findOneById(PLAYER_ID)).thenReturn(Optional.of(playerMock));
        when(mockedPlayerTrainingDeclineEventWriteRepository.save(any())).thenReturn(playerTrainingDeclineEventMock);

        try (
            MockedStatic<EventId> eventIdMockedStatic = Mockito.mockStatic(EventId.class);
            MockedStatic<InstantProvider> instantMockedStatic = Mockito.mockStatic(InstantProvider.class);
        ) {
            eventIdMockedStatic.when(EventId::generate).thenReturn(EVENT_ID);
            instantMockedStatic.when(InstantProvider::now).thenReturn(NOW);

            // Act
            generateSingleDeclineTrainingUseCaseMock.generate(PLAYER_ID, CURRENT_DAY, DECLINE_SPEED);

            eventIdMockedStatic.verify(EventId::generate);
            eventIdMockedStatic.verifyNoMoreInteractions();
            instantMockedStatic.verify(InstantProvider::now);
            instantMockedStatic.verifyNoMoreInteractions();
        }

        // Assert
        ArgumentCaptor<PlayerTrainingDeclineEvent> argumentCaptor = ArgumentCaptor.forClass(PlayerTrainingDeclineEvent.class);
        verify(mockedPlayerTrainingDeclineEventWriteRepository)
            .save(argumentCaptor.capture());

        PlayerTrainingDeclineEvent playerTrainingDeclineEvent = argumentCaptor.getValue();

        assertEquals(PLAYER_ID, playerTrainingDeclineEvent.getPlayerId());
        assertEquals(DECLINE_SPEED, playerTrainingDeclineEvent.getDeclineSpeed());
        assertEquals(CURRENT_DAY, playerTrainingDeclineEvent.getCurrentDay());
        assertEquals(EVENT_ID, playerTrainingDeclineEvent.getId());
        assertEquals(NOW, playerTrainingDeclineEvent.getOccurredAt());
        verify(mockedPlayerReadRepository).findOneById(PLAYER_ID);

        verify(playerMock).addDeclinePhase(playerTrainingDeclineEventMock);
        verify(mockedPlayerReadRepository).findOneById(PLAYER_ID);
        verify(mockedPlayerTrainingDeclineEventWriteRepository).save(any());
        verify(mockedPlayerWriteRepository).save(playerMock);
        verifyNoMoreInteractions(playerMock, mockedPlayerReadRepository, mockedPlayerTrainingDeclineEventWriteRepository);
    }

     */
}
