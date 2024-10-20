package com.kjeldsen.player.domain.events;

import com.kjeldsen.domain.Event;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@Document(collection = "ExpenseEvent")
@TypeAlias("ExpenseEvent")
public class ExpenseEvent extends Event { // TODO remove replaces by TransactionEvent

    private Team.TeamId teamId;
    private Player.PlayerId playerId;
    private BigDecimal amount;

}
