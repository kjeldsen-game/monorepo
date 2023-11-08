package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.IncomeEvent;
import com.kjeldsen.player.domain.repositories.IncomeEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.IncomeEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncomeEventWriteRepositoryMongoAdapter implements IncomeEventWriteRepository {

    private final IncomeEventMongoRepository incomeEventMongoRepository;

    @Override
    public IncomeEvent save(IncomeEvent incomeEvent) {
        return incomeEventMongoRepository.save(incomeEvent);
    }

}
