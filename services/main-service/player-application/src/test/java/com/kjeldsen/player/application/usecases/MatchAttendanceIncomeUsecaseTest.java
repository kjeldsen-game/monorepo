package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.application.usecases.economy.MatchAttendanceIncomeUsecase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MatchAttendanceIncomeUsecaseTest {

    private final CreateTransactionUseCase mockedCreateTransactionUseCase = Mockito.mock(CreateTransactionUseCase.class);
    private final MatchAttendanceIncomeUsecase matchAttendanceIncomeUsecase = new MatchAttendanceIncomeUsecase(
            mockedCreateTransactionUseCase);

    private static Team.TeamId mockedTeamId;

    @BeforeAll
    static void beforeAllSetUp() {
        mockedTeamId = TestData.generateTestTeamId();
    }

    @Test
    @DisplayName("Should throw exception when attendance is negative")
    public void should_throw_exception_when_attendance_is_negative() {
        assertEquals("Attendance count cannot be negative", assertThrows(
            IllegalArgumentException.class, () -> {
                matchAttendanceIncomeUsecase.income(mockedTeamId, -1);}).getMessage());
    }

    @Test
    @DisplayName("Should pass right values to the CreateTransactionUseCase")
    public void should_pass_right_values_to_the_CreateTransactionUseCase() {
        matchAttendanceIncomeUsecase.income(mockedTeamId, 10000);

        verify(mockedCreateTransactionUseCase, times(1)).create(
                eq(mockedTeamId),
                eq(BigDecimal.valueOf(1_000_000)),
                eq(Transaction.TransactionType.ATTENDANCE)
        );
    }
}