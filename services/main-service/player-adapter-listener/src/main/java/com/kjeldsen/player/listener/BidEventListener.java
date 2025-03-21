package com.kjeldsen.player.listener;

import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.BidEvent;
import com.kjeldsen.player.domain.exceptions.InsufficientBalanceException;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class BidEventListener {

    private final GetTeamUseCase getTeamUseCase;
    private final TeamWriteRepository teamWriteRepository;

    @EventListener
    public void handleAuctionEndEvent(BidEvent bidEvent) {
        log.info("BidEvent received: {}", bidEvent);
        Team team = getTeamUseCase.get(Team.TeamId.of(bidEvent.getTeamId()));
        updateTeamBalance(team, bidEvent.getAmount());
        teamWriteRepository.save(team);
    }

    private void updateTeamBalance(Team team, BigDecimal bidAmountDiff) {
        if (team.getEconomy().getBalance().compareTo(bidAmountDiff.abs()) < 0) {
            throw new InsufficientBalanceException();
        }
        team.getEconomy().updateBalance(bidAmountDiff);
    }

}
