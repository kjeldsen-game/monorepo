package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.domain.EventId;
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

    private final TeamReadRepository teamReadRepository;
    private final TeamWriteRepository teamWriteRepository;
    private final PricingEventWriteRepository pricingEventWriteRepository;

    public void update(Team.TeamId teamId, Integer newPrice, Team.Economy.PricingType pricingType) {
        log.info("UpdateTeamPricingUsecase team {}", teamId);

        Team team = teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        if (newPrice < pricingType.getMinPrice() || newPrice > pricingType.getMaxPrice()) {
            throw new IllegalArgumentException("Price cannot be lower or higher than max or min price of item");
        }

        PricingEvent pricingEvent = PricingEvent.builder()
            .id(EventId.generate())
            .occurredAt(InstantProvider.now())
            .teamId(teamId)
            .pricingType(pricingType)
            .price(newPrice)
            .build();

        pricingEventWriteRepository.save(pricingEvent);
        team.getEconomy().updatePrices(pricingEvent);
        teamWriteRepository.save(team);
    }

}
