package com.kjeldsen.player.application.usecases.cantera;


import com.kjeldsen.player.domain.Team;
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

    public void investToCanteraCategory(Team.TeamId teamId, Team.Cantera.Investment canteraInvestment, Integer points) {
        log.info("{} investment team {} with {} points", canteraInvestment, teamId, points);

        Team team = teamReadRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        switch (canteraInvestment) {
            case BUILDINGS -> team.getCantera().addBuildingsInvestment(points);
            case ECONOMY -> team.getCantera().addEconomyInvestment(points);
            case TRADITION -> team.getCantera().addTraditionInvestment(points);
            default -> throw new IllegalArgumentException("Unknown investment type: " + canteraInvestment);
        }
        teamWriteRepository.save(team);
    }
}
