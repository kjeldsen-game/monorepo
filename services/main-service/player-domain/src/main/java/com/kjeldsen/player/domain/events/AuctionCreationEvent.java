package com.kjeldsen.player.domain.events;

import com.kjeldsen.domain.Event;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@SuperBuilder
@Document(collection = "AuctionCreationEvents")
@TypeAlias("AuctionCreation")
public class AuctionCreationEvent extends Event {
    Player.PlayerId playerId;
    Team.TeamId teamId;
}

