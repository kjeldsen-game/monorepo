package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class RestaurantIncomeUseCaseTest {

    private final TeamReadRepository mockedTeamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final CreateTransactionUseCase mockedCreateTransactionUseCase = Mockito.mock(CreateTransactionUseCase.class);
    private final RestaurantIncomeUseCase restaurantIncomeUseCase = new RestaurantIncomeUseCase(mockedTeamReadRepository,
            mockedCreateTransactionUseCase);

    private static Team.TeamId mockedTeamId;

    @BeforeAll
    static void setUpBeforeClass() {
        mockedTeamId = TestData.generateTestTeamId();
    }

    @Test
    @DisplayName("Should throw exception when team is null")
    public void should_throw_exception_when_team_is_null() {
        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.empty());

        assertEquals("Team not found", assertThrows(RuntimeException.class, () ->
                restaurantIncomeUseCase.income(mockedTeamId, 5000 )).getMessage());

        verify(mockedTeamReadRepository).findById(mockedTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository, mockedCreateTransactionUseCase);
    }

    @Test
    @DisplayName("Should pass right values to Transaction UseCase")
    public void should_pass_right_values_to_Transaction_UseCase() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        mockedTeam.getEconomy().setBalance(BigDecimal.ZERO);

        when(mockedTeamReadRepository.findById(mockedTeamId)).thenReturn(Optional.of(mockedTeam));

        restaurantIncomeUseCase.income(mockedTeamId, 5000);

        BigDecimal calculatedPrice = BigDecimal.valueOf(5000);

        verify(mockedCreateTransactionUseCase, times(1)).create(
            eq(mockedTeamId),
            eq(calculatedPrice),
            eq(Transaction.TransactionType.RESTAURANT)
        );

        verify(mockedTeamReadRepository).findById(mockedTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository, mockedCreateTransactionUseCase);
    }
}