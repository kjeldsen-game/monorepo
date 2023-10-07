package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.EconomyInvestmentEvent;

public interface EconomyInvestmentEventWriteRepository {
    EconomyInvestmentEvent save(EconomyInvestmentEvent economyInvestmentEvent);
}
