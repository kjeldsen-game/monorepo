package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import org.mapstruct.Mapper;

@Mapper
public interface IdMapper {

    default String map(Player.PlayerId playerId) {
        return playerId.value();
    }

    default String map(Team.TeamId teamId) {
        if (teamId == null) {
            return null;
        }
        return teamId.value();
    }

    default String map(Transaction.TransactionId transactionId) {
        return transactionId.value();
    }
}
