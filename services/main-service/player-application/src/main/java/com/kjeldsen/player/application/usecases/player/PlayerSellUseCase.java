package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.lib.events.AuctionCreationEvent;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerStatus;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PlayerSellUseCase {

    private final PlayerWriteRepository playerWriteRepository;
    private final GetPlayersUseCase getPlayersUseCase;

    public com.kjeldsen.lib.events.AuctionCreationEvent sell(Player.PlayerId playerId) {
        log.info("PlayerSellUseCase for player {}", playerId);
        Player player = getPlayersUseCase.get(playerId);

        if (player.getStatus() == PlayerStatus.FOR_SALE) {
            throw new RuntimeException("Player's status is already FOR_SALE");
        }

        player.setStatus(PlayerStatus.FOR_SALE);
        playerWriteRepository.save(player);

        return AuctionCreationEvent.builder()
                .playerId(playerId.value()).teamId(player.getTeamId().value()).build();
    }
}
