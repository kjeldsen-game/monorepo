package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.IncomeEvent;

public interface IncomeEventWriteRepository {
    IncomeEvent save(IncomeEvent incomeEvent);
}
