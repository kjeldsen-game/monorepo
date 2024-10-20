package com.kjeldsen.player.domain.events;

import com.kjeldsen.domain.Event;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@ToString
@Document(collection = "AuctionEndEvents")
@TypeAlias("AuctionEndEvent")
public class AuctionEndEvent extends Event {
    Player.PlayerId playerId;
    BigDecimal amount;
    Team.TeamId auctionWinner;
    Team.TeamId auctionCreator;
}
