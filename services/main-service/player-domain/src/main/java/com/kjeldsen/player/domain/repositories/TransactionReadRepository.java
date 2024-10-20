package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import java.util.List;

public interface TransactionReadRepository {

    List<Transaction> findAllByTeamId(Team.TeamId teamId);
}
