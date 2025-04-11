package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.application.usecases.economy.CreateTransactionUseCase;
import com.kjeldsen.player.application.usecases.economy.MerchandiseIncomeUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.Mockito.*;

class MerchandiseIncomeUseCaseTest {

    private final GetTeamUseCase mockedGetTeamUseCase = Mockito.mock(GetTeamUseCase.class);
    private final CreateTransactionUseCase mockedCreateTransactionUseCase = Mockito.mock(CreateTransactionUseCase.class);
    private final MerchandiseIncomeUseCase merchandiseIncomeUseCase = new MerchandiseIncomeUseCase(mockedGetTeamUseCase,
            mockedCreateTransactionUseCase);

    private static Team.TeamId mockedTeamId;

    @BeforeAll
    static void setUpBeforeClass() {
        mockedTeamId = TestData.generateTestTeamId();
    }


    @Test
    @DisplayName("Should pass right values to Transaction UseCase")
    public void should_pass_right_values_to_Transaction_UseCase() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        mockedTeam.getEconomy().setBalance(BigDecimal.ZERO);
        mockedTeam.getEconomy().setPrices(Map.of(
                Team.Economy.PricingType.MERCHANDISE, 5
        ));

        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        merchandiseIncomeUseCase.income(mockedTeamId, 5000);

        BigDecimal calculatedPrice = BigDecimal.valueOf(1250);

        verify(mockedCreateTransactionUseCase, times(1)).create(
                eq(mockedTeamId),
                eq(calculatedPrice),
                eq(Transaction.TransactionType.MERCHANDISE)
        );

        verify(mockedGetTeamUseCase).get(mockedTeamId);
        verifyNoMoreInteractions(mockedGetTeamUseCase, mockedCreateTransactionUseCase);
    }
}