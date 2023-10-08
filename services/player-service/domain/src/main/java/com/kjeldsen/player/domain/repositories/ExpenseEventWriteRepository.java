package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.ExpenseEvent;

public interface ExpenseEventWriteRepository {
    ExpenseEvent save(ExpenseEvent expenseEvent);
}
