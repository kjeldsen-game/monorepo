package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.ExpenseEvent;
import com.kjeldsen.player.domain.repositories.ExpenseEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.ExpenseEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseEventWriteRepositoryMongoAdapter implements ExpenseEventWriteRepository {

    private final ExpenseEventMongoRepository expenseEventMongoRepository;

    @Override
    public ExpenseEvent save(ExpenseEvent expenseEvent) {
        return expenseEventMongoRepository.save(expenseEvent);
    }

}
