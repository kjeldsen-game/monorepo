package com.kjeldsen.player.application.usecases.cantera;


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
@Component
@RequiredArgsConstructor
public class CanteraInvestmentUsecase {

    private final TeamReadRepository teamReadRepository;
    private final TeamWriteRepository teamWriteRepository;
    private final CanteraInvestmentEventWriteRepository canteraInvestmentEventWriteRepository;

    public void investToCanteraCategory(Team.TeamId teamId, Team.Cantera.Investment canteraInvestment, Integer points) {
        log.info("{} investment team {} with {} points", canteraInvestment, teamId, points);

        Team team = teamReadRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        CanteraInvestmentEvent canteraInvestmentEvent = CanteraInvestmentEvent.builder()
                .id(EventId.generate())
                .occurredAt(InstantProvider.now())
                .teamId(teamId)
                .investment(canteraInvestment)
                .points(points)
                .build();

        canteraInvestmentEventWriteRepository.save(canteraInvestmentEvent);

        switch (canteraInvestment) {
            case BUILDINGS -> team.getCantera().addBuildingsInvestment(canteraInvestmentEvent);
            case ECONOMY -> team.getCantera().addEconomyInvestment(canteraInvestmentEvent);
            case TRADITION -> team.getCantera().addTraditionInvestment(canteraInvestmentEvent);
            default -> throw new IllegalArgumentException("Unknown investment type: " + canteraInvestment);
        }
        teamWriteRepository.save(team);
    }
}
