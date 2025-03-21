package com.kjeldsen.player.application.usecases.economy;


import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import com.kjeldsen.player.domain.repositories.TransactionWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateTransactionUseCase {

    private final TeamWriteRepository teamWriteRepository;
    private final TransactionWriteRepository transactionWriteRepository;
    private final GetTeamUseCase getTeamUseCase;


    public void create(Team.TeamId teamId, BigDecimal amount, Transaction.TransactionType transactionType) {
        log.info("CreateTransactionUseCase for {} with amount {} and type {}", teamId, amount, transactionType);

        Team team = getTeamUseCase.get(teamId);

        Transaction transaction = createTransaction(team, amount, transactionType);

        team.getEconomy().updateBalance(amount);
        transactionWriteRepository.save(transaction);
        teamWriteRepository.save(team);
    }

    public void create(Team.TeamId teamId, BigDecimal amount, String playerId) {
        log.info("CreateTransactionUseCase for {} with amount {} for playerId {}", teamId, amount, playerId);

        Team team = getTeamUseCase.get(teamId);

        Transaction transaction = createTransaction(team, amount,
            Transaction.TransactionType.PLAYER_WAGE);
        transaction.setMessage(playerId);

        team.getEconomy().updateBalance(amount);
        transactionWriteRepository.save(transaction);
        teamWriteRepository.save(team);
    }

    private Transaction createTransaction(Team team, BigDecimal amount, Transaction.TransactionType transactionType) {
        System.out.println(amount);
        return  Transaction.builder()
            .teamId(team.getId())
            .transactionType(transactionType)
            .transactionAmount(amount)
            .prevTransactionBalance(team.getEconomy().getBalance())
            .postTransactionBalance(team.getEconomy().getBalance().add(amount))
            .build();
    }
}
