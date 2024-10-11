package com.kjeldsen.player.application.usecases;


import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.events.TransactionEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import com.kjeldsen.player.domain.repositories.TransactionEventWriteRepository;
import com.kjeldsen.player.domain.repositories.TransactionWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateTransactionUseCase {

    private final TeamReadRepository teamReadRepository;
    private final TeamWriteRepository teamWriteRepository;
    private final TransactionWriteRepository transactionWriteRepository;
    private final TransactionEventWriteRepository transactionEventWriteRepository;

    public void create(Team.TeamId teamId, BigDecimal amount, Transaction.TransactionType transactionType) {
        log.info("CreateTransactionUseCase for {} with amount {} and type {}", teamId, amount, transactionType);

        Team team =  teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        TransactionEvent transactionEvent = TransactionEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .teamId(team.getId())
            .transactionType(transactionType)
            .transactionAmount(amount)
            .prevTransactionBalance(team.getEconomy().getBalance())
            .postTransactionBalance(team.getEconomy().getBalance().add(amount))
            .build();

        team.getEconomy().updateBalance(amount);

        transactionEventWriteRepository.save(transactionEvent);
        transactionWriteRepository.save(Transaction.creation(transactionEvent));
        teamWriteRepository.save(team);
    }
}