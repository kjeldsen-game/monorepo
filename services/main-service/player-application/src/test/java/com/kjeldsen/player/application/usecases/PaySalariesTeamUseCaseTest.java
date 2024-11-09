package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.application.usecases.economy.CreateTransactionUseCase;
import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.repositories.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PaySalariesTeamUseCaseTest {

    final private PlayerReadRepository playerReadRepository = Mockito.mock(PlayerReadRepository.class);
    final private CreateTransactionUseCase mockedCreateTransactionUseCase = Mockito.mock(CreateTransactionUseCase.class);
    final private PaySalariesTeamUseCase paySalariesTeamUseCase = new PaySalariesTeamUseCase(
            playerReadRepository,mockedCreateTransactionUseCase );

    private static Team.TeamId mockedTeamId;
    private Team mockedTeam;
    private List<Player> mockedPlayers;

    @BeforeAll
    public static void setUpBeforeClass() {
        mockedTeamId = TestData.generateTestTeamId();
    }

    @BeforeEach
    public void setup() {
        mockedTeam = TestData.generateTestTeam(mockedTeamId);
        mockedPlayers = TestData.generateTestPlayers(mockedTeamId, 50);
    }

    @Test
    @DisplayName("Should throw error if player salary is negative")
    public void should_throw_error_if_player_salary_is_negative() {
        mockedPlayers.get(10).setEconomy(Player.Economy.builder().salary(BigDecimal.valueOf(-1)).build());
        when(playerReadRepository.findByTeamId(mockedTeamId)).thenReturn(mockedPlayers);

        assertEquals("Salary cannot be negative", assertThrows(
                IllegalArgumentException.class, () -> {
                    paySalariesTeamUseCase.pay(mockedTeamId);}).getMessage());
    }

    @Test
    @DisplayName("Should pass right values to the CreateTransactionUseCase")
    public void should_throw_exception_when_team_does_not_exist(){
        setMockedPlayersSalary(BigDecimal.valueOf(1000));
        when(playerReadRepository.findByTeamId(mockedTeamId)).thenReturn(mockedPlayers);
        paySalariesTeamUseCase.pay(mockedTeamId);

        verify(mockedCreateTransactionUseCase, times(50)).create(
            eq(mockedTeamId),
            eq(BigDecimal.valueOf(-1000)),
            anyString()
        );
    }

    private void setMockedPlayersSalary(BigDecimal salary) {
        mockedPlayers.forEach(player -> player.setEconomy(Player.Economy.builder().salary(salary).build()));
    }
}
