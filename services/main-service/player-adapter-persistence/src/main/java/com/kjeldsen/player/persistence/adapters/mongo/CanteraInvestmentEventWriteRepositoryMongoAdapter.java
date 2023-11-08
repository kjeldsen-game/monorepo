package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.CanteraInvestmentEvent;
import com.kjeldsen.player.domain.repositories.CanteraInvestmentEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.CanteraInvestmentEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CanteraInvestmentEventWriteRepositoryMongoAdapter implements CanteraInvestmentEventWriteRepository {

    private final CanteraInvestmentEventMongoRepository canteraInvestmentEventMongoRepository;

    @Override
    public CanteraInvestmentEvent save(CanteraInvestmentEvent canteraInvestmentEvent) {
        return canteraInvestmentEventMongoRepository.save(canteraInvestmentEvent);
    }
}
