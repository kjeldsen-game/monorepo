package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.player.domain.events.AuctionCreationEvent;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerStatus;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PlayerSellUseCase {

    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;

    public AuctionCreationEvent sell(Player.PlayerId playerId) {
        log.info("PlayerSellUseCase for player {}", playerId);
        Player player = playerReadRepository.findOneById(playerId)
            .orElseThrow(() -> new RuntimeException("Player not found"));

        if (player.getStatus() == PlayerStatus.FOR_SALE) {
            throw new RuntimeException("Player's status is already FOR_SALE");
        }

        player.setStatus(PlayerStatus.FOR_SALE);
        playerWriteRepository.save(player);

        return AuctionCreationEvent.builder()
            .playerId(playerId).teamId(player.getTeamId()).build();
    }
}
