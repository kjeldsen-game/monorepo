package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.CanteraInvestmentEvent;

public interface CanteraInvestmentEventWriteRepository {
    CanteraInvestmentEvent save(CanteraInvestmentEvent canteraInvestmentEvent);
}
