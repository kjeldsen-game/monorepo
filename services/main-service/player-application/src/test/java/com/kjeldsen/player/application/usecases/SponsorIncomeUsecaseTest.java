package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.application.usecases.economy.CreateTransactionUseCase;
import com.kjeldsen.player.application.usecases.economy.SponsorIncomeUsecase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import static org.mockito.Mockito.*;


class SponsorIncomeUsecaseTest {

    private final CreateTransactionUseCase mockedCreateTransactionUseCase = Mockito.mock(CreateTransactionUseCase.class);
    private final SponsorIncomeUsecase sponsorIncomeUsecase = new SponsorIncomeUsecase(mockedCreateTransactionUseCase);

    private static Team.TeamId mockedTeamId;

    @BeforeAll
    static void beforeAllSetUp() {
        mockedTeamId = TestData.generateTestTeamId();
    }

    @Test
    @DisplayName("Should create a Transaction with right amount of money in Parameters")
    public void should_throw_error_when_attendance_is_negative() {
        sponsorIncomeUsecase.incomeWeekly(mockedTeamId, Team.Economy.IncomeMode.CONSERVATIVE, 5);

        verify(mockedCreateTransactionUseCase, times(1)).create(
            eq(mockedTeamId),
            eq(BigDecimal.valueOf(100_000)),
            eq(Transaction.TransactionType.SPONSOR)
        );

        sponsorIncomeUsecase.incomeAnnual(mockedTeamId, Team.Economy.IncomeMode.CONSERVATIVE, 5);

        verify(mockedCreateTransactionUseCase, times(1)).create(
            eq(mockedTeamId),
            eq(BigDecimal.valueOf(1_500_000)),
            eq(Transaction.TransactionType.SPONSOR)
        );
    }
}