package com.kjeldsen.player.application.usecases;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.events.IncomeEvent;
import com.kjeldsen.player.domain.events.TransactionEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.IncomeEventWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;

import com.kjeldsen.player.domain.repositories.TransactionWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchAttendanceIncomeUsecase {

    private final CreateTransactionUseCase createTransactionUseCase;

    public void income(Team.TeamId teamId, Integer attendanceCount) {
        log.info("MatchIncomeAttendanceUsecase team {}", teamId);

        if (attendanceCount < 0 ) {
            throw new IllegalArgumentException("Attendance count cannot be negative");
        }

        BigDecimal amount = getAmount(attendanceCount, 100);
        createTransactionUseCase.create(teamId, amount, Transaction.TransactionType.ATTENDANCE);


    }

    private BigDecimal getAmount(Integer attendanceCount, Integer price) {
        // TODO to be adjusted to calc the amount based on tickets
        return BigDecimal.valueOf(attendanceCount * price);
    }
}
