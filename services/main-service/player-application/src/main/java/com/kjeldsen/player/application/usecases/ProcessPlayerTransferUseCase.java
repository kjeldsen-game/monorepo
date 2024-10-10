package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerStatus;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProcessPlayerTransferUseCase {

    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;
    private final CreateTransactionUseCase createTransactionUseCase;

    public void process(Player.PlayerId playerId, BigDecimal amount, Team.TeamId winner, Team.TeamId creator) {

        Player player = playerReadRepository.findOneById(playerId).orElseThrow(
            () -> new RuntimeException("Player not found"));

        player.setStatus(PlayerStatus.BENCH);
        player.setTeamId(winner);
        createTransactionUseCase.create(winner, amount.negate(), Transaction.TransactionType.PLAYER_PURCHASE);
        createTransactionUseCase.create(creator, amount, Transaction.TransactionType.PLAYER_SALE);

        playerWriteRepository.save(player);
    }
}
