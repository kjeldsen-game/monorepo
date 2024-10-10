package com.kjeldsen.player.listener;

import com.kjeldsen.player.application.usecases.*;
import com.kjeldsen.player.domain.events.AuctionEndEvent;
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

        processPlayerTransferUseCase.process(auctionEndEvent.getPlayerId(), auctionEndEvent.getAmount(),
            auctionEndEvent.getAuctionWinner(), auctionEndEvent.getAuctionCreator());
    }
}
