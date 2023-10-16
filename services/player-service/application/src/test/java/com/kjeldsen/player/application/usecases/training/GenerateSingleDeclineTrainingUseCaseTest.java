package com.kjeldsen.player.application.usecases.training;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerTrainingDeclineEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class GenerateSingleDeclineTrainingUseCaseTest {

    private static final Integer DECLINE_SPEED = 50;
    private static final Integer CURRENT_DAY = 4;
    private static final Player.PlayerId PLAYER_ID = mock(Player.PlayerId.class);

    final private PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    final private PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    final private PlayerTrainingDeclineEventWriteRepository mockedPlayerTrainingDeclineEventWriteRepository = Mockito.mock(
        PlayerTrainingDeclineEventWriteRepository.class);
    private final GenerateSingleDeclineTrainingUseCase generateSingleDeclineTrainingUseCase =
        new GenerateSingleDeclineTrainingUseCase(mockedPlayerTrainingDeclineEventWriteRepository,
            mockedPlayerReadRepository, mockedPlayerWriteRepository);

    @Test
    @DisplayName("Generate inside GenerateSingleDeclineTrainingUseCase should throw an exception when player does not exist")
    public void generate_Should_Throw_Exception_When_Player_Does_Not_Exist() {

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> generateSingleDeclineTrainingUseCase.generate(PLAYER_ID, CURRENT_DAY, DECLINE_SPEED));
        assertEquals("Player not found.", exception.getMessage());
    }

}
