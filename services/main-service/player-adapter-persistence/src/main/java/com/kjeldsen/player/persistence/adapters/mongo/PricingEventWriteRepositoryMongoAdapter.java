package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.PricingEvent;
import com.kjeldsen.player.domain.repositories.PricingEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PricingEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PricingEventWriteRepositoryMongoAdapter implements PricingEventWriteRepository {
    private final PricingEventMongoRepository pricingEventMongoRepository;

    @Override
    public PricingEvent save(PricingEvent pricingEvent) {
        return pricingEventMongoRepository.save(pricingEvent);
    }
}