package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.application.usecases.economy.CreateTransactionUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerStatus;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProcessPlayerTransferUseCase {

    private final PlayerWriteRepository playerWriteRepository;
    private final TeamWriteRepository teamWriteRepository;
    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetPlayersUseCase getPlayersUseCase;
    private final GetTeamUseCase getTeamUseCase;

    public void process(Player.PlayerId playerId, BigDecimal amount, Team.TeamId winner, Team.TeamId creator) {
        log.info("ProcessPlayerTransferUseCase for player {}, winner {}, creator {}, amount {}", playerId, winner, creator, amount);

        Player player = getPlayersUseCase.get(playerId);

        player.setStatus(PlayerStatus.BENCH);
        player.setTeamId(winner);
        createTransactionUseCase.create(winner, amount.negate(), Transaction.TransactionType.PLAYER_PURCHASE);
        createTransactionUseCase.create(creator, amount, Transaction.TransactionType.PLAYER_SALE);

        playerWriteRepository.save(player);
    }

    public void processMoneyReturn(Map<String, BigDecimal> bids) {
        List<Team> teams = getTeamUseCase.getTeamsByIds(bids.keySet().stream()
            .map(Team.TeamId::new).collect(Collectors.toList()));
        System.out.println(teams.size());
        for (Team team : teams) {
            try {
                team.getEconomy().updateBalance(bids.get(team.getName()));
                teamWriteRepository.save(team);
            } catch (Exception e) {
                log.error(e.getMessage());

            }
        }
    }
}
