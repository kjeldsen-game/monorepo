package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillboardIncomeUseCaseTest {

    private final GetTeamUseCase mockedGetTeamUseCase = Mockito.mock(GetTeamUseCase.class);
    private final CreateTransactionUseCase mockedCreateTransactionUseCase = Mockito.mock(CreateTransactionUseCase.class);
    private final BillboardIncomeUseCase billboardIncomeUseCase = new BillboardIncomeUseCase(mockedGetTeamUseCase,
        mockedCreateTransactionUseCase);

    private static Team.TeamId testTeamId;

    @BeforeAll
    static void setUp() {
        testTeamId = TestData.generateTestTeamId();
    }


    @Test
    @DisplayName("Should throw exception when billboard deal is null")
    void should_throw_exception_when_billboard_deal_is_null() {
        Team mockedTeam = Mockito.mock(Team.class);
        Team.Economy mockedEconomy = Mockito.mock(Team.Economy.class);

        when(mockedGetTeamUseCase.get(testTeamId)).thenReturn(mockedTeam);
        when(mockedTeam.getEconomy()).thenReturn(mockedEconomy);
        when(mockedEconomy.getBillboardDeal()).thenReturn(null);

        assertEquals("BillboardDeal not found", assertThrows(RuntimeException.class, () -> {
            billboardIncomeUseCase.pay(testTeamId);
        }).getMessage());
        verify(mockedGetTeamUseCase).get(testTeamId);
        verifyNoMoreInteractions(mockedGetTeamUseCase);
    }

    @Test
    @DisplayName("Should pass the variables to create transaction usecase")
    void should_pass_variables_to_create_transaction() {
        Team mockedTeam = mock(Team.class);
        Team.Economy mockedEconomy = mock(Team.Economy.class);

        when(mockedGetTeamUseCase.get(testTeamId)).thenReturn(mockedTeam);
        when(mockedTeam.getEconomy()).thenReturn(mockedEconomy);
        when(mockedEconomy.getBillboardDeal()).thenReturn(Team.Economy.BillboardDeal.builder().offer(
            BigDecimal.valueOf(100)).build());

        billboardIncomeUseCase.pay(testTeamId);

        verify(mockedCreateTransactionUseCase).create(eq(testTeamId), eq(BigDecimal.valueOf(100)),
            eq(Transaction.TransactionType.BILLBOARDS));
    }
}