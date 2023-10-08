package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.EconomyInvestmentEvent;
import com.kjeldsen.player.domain.repositories.EconomyInvestmentEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.EconomyInvestmentEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EconomyInvestmentEventWriteRepositoryMongoAdapter implements EconomyInvestmentEventWriteRepository {

    private final EconomyInvestmentEventMongoRepository economyInvestmentEventMongoRepository;

    @Override
    public EconomyInvestmentEvent save(EconomyInvestmentEvent economyInvestmentEvent) {
        return economyInvestmentEventMongoRepository.save(economyInvestmentEvent);
    }
}
