package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.application.testdata.TestData;
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

    private final TeamReadRepository mockedTeamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final CreateTransactionUseCase mockedCreateTransactionUseCase = Mockito.mock(CreateTransactionUseCase.class);
    private final BillboardIncomeUseCase billboardIncomeUseCase = new BillboardIncomeUseCase(mockedTeamReadRepository,
        mockedCreateTransactionUseCase);

    private static Team.TeamId testTeamId;

    @BeforeAll
    static void setUp() {
        testTeamId = TestData.generateTestTeamId();
    }

    @Test
    @DisplayName("Should throw exception when team is null")
    void should_throw_exception_when_team_is_null() {
        when(mockedTeamReadRepository.findById(testTeamId)).thenReturn(Optional.empty());

        assertEquals("Team not found", assertThrows(RuntimeException.class, () -> {
            billboardIncomeUseCase.pay(testTeamId);
        }).getMessage());

        verify(mockedTeamReadRepository).findById(testTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }

    @Test
    @DisplayName("Should throw exception when billboard deal is null")
    void should_throw_exception_when_billboard_deal_is_null() {
        Team mockedTeam = Mockito.mock(Team.class);
        Team.Economy mockedEconomy = Mockito.mock(Team.Economy.class);

        when(mockedTeamReadRepository.findById(testTeamId)).thenReturn(Optional.of(mockedTeam));
        when(mockedTeam.getEconomy()).thenReturn(mockedEconomy);
        when(mockedEconomy.getBillboardDeal()).thenReturn(null);

        assertEquals("BillboardDeal not found", assertThrows(RuntimeException.class, () -> {
            billboardIncomeUseCase.pay(testTeamId);
        }).getMessage());
        verify(mockedTeamReadRepository).findById(testTeamId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }

    @Test
    @DisplayName("Should pass the variables to create transaction usecase")
    void should_pass_variables_to_create_transaction() {
        Team mockedTeam = mock(Team.class);
        Team.Economy mockedEconomy = mock(Team.Economy.class);

        when(mockedTeamReadRepository.findById(testTeamId)).thenReturn(Optional.of(mockedTeam));
        when(mockedTeam.getEconomy()).thenReturn(mockedEconomy);
        when(mockedEconomy.getBillboardDeal()).thenReturn(Team.Economy.BillboardDeal.builder().offer(
            BigDecimal.valueOf(100)).build());

        billboardIncomeUseCase.pay(testTeamId);

        verify(mockedCreateTransactionUseCase).create(eq(testTeamId), eq(BigDecimal.valueOf(100)),
            eq(Transaction.TransactionType.BILLBOARDS));
    }
}