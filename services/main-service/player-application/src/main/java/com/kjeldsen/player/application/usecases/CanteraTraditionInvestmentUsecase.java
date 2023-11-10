package com.kjeldsen.player.application.usecases;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.CanteraInvestmentEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.CanteraInvestmentEventWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CanteraTraditionInvestmentUsecase {

    private final TeamReadRepository teamReadRepository;
    private final TeamWriteRepository teamWriteRepository;
    private final CanteraInvestmentEventWriteRepository canteraInvestmentEventWriteRepository;

    public void invest(Team.TeamId teamId, Integer points) {
        log.info("Tradition investment team {} with {} points", teamId, points);

        Team team = teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        CanteraInvestmentEvent canteraInvestmentEvent = CanteraInvestmentEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .teamId(teamId)
            .investment(Team.Cantera.Investment.TRADITION)
            .points(points)
            .build();

        canteraInvestmentEventWriteRepository.save(canteraInvestmentEvent);

        team.getCantera().addTraditionInvestment(canteraInvestmentEvent);

        teamWriteRepository.save(team);
    }

}
