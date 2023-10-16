package com.kjeldsen.player.application.usecases.training;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingEventReadRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GetHistoricalTrainingUseCaseTest {

    private final PlayerTrainingEventReadRepository mockPlayerTrainingEventReadRepository = Mockito.mock(PlayerTrainingEventReadRepository.class);
    private final PlayerReadRepository mockPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final GetHistoricalTrainingUseCase getHistoricalTrainingUseCase = new GetHistoricalTrainingUseCase(mockPlayerTrainingEventReadRepository, mockPlayerReadRepository);

    @Test
    public void should_generate_runtimeException_if_player_not_found() {
        Player.PlayerId playerId = Player.PlayerId.generate();
        when(mockPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            getHistoricalTrainingUseCase.get(playerId);
        });
        assertEquals(String.format("Player not found with ID %s", playerId), exception.getMessage());
        verify(mockPlayerReadRepository).findOneById(playerId);
        verifyNoMoreInteractions(mockPlayerReadRepository);
    }

    @Test
    public void should_generate_a_list_of_training_events() {
        Player.PlayerId playerId = Player.PlayerId.generate();
        Player playerMock = mock(Player.class);
        List<PlayerTrainingEvent> trainingEvents = new ArrayList<>();
        when(mockPlayerReadRepository.findOneById(playerId)).thenReturn(Optional.of(playerMock));
        when(mockPlayerTrainingEventReadRepository.findAllByPlayerId(playerId)).thenReturn(trainingEvents);
        List<PlayerTrainingEvent> result = getHistoricalTrainingUseCase.get(playerId);
        assertEquals(trainingEvents, result);
        verify(mockPlayerReadRepository).findOneById(playerId);
        verify(mockPlayerTrainingEventReadRepository).findAllByPlayerId(playerId);
        verifyNoMoreInteractions(mockPlayerReadRepository, mockPlayerTrainingEventReadRepository);
    }
}
