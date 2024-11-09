package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.PricingEvent;

public interface PricingEventWriteRepository {
    PricingEvent save(PricingEvent pricingEvent);

}
