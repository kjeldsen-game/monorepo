package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PricingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PricingEventWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateTeamPricingUsecase {

    private final TeamWriteRepository teamWriteRepository;
    private final GetTeamUseCase getTeamUseCase;

    public void update(Team.TeamId teamId, Integer newPrice, Team.Economy.PricingType pricingType) {
        log.info("UpdateTeamPricingUsecase team {} for pricingType {} with new price {}", teamId, pricingType, newPrice);

        Team team = getTeamUseCase.get(teamId);

        if (newPrice < pricingType.getMinPrice() || newPrice > pricingType.getMaxPrice()) {
            throw new IllegalArgumentException("Price cannot be lower or higher than max or min price of item");
        }

        team.getEconomy().updatePrices(pricingType, newPrice);
        teamWriteRepository.save(team);
    }
}
