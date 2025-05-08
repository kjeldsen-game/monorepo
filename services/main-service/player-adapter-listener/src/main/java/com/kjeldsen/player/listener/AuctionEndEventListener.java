package com.kjeldsen.player.listener;

import com.kjeldsen.lib.events.AuctionEndEvent;
import com.kjeldsen.player.application.usecases.player.ProcessPlayerTransferUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuctionEndEventListener {

    private final ProcessPlayerTransferUseCase processPlayerTransferUseCase;

    @EventListener
    public void handleAuctionEndEvent(AuctionEndEvent auctionEndEvent) {
        log.info("AuctionEndEvent received: {}", auctionEndEvent);

        processPlayerTransferUseCase.process(
            Player.PlayerId.of(auctionEndEvent.getPlayerId()),
            auctionEndEvent.getAmount(),
            Team.TeamId.of(auctionEndEvent.getAuctionWinner()),
            Team.TeamId.of(auctionEndEvent.getAuctionCreator()));
    }
}
