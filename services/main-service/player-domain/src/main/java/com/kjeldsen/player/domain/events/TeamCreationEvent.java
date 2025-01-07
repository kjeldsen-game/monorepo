package com.kjeldsen.player.domain.events;

import com.kjeldsen.domain.Event;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@Document(collection = "AuctionCreationEvents")
@TypeAlias("AuctionCreationEvent")
public class TeamCreationEvent extends Event {
    String teamId;
    BigDecimal teamValue;
}
