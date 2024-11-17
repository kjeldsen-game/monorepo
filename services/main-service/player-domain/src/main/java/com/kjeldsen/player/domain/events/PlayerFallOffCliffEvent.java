package com.kjeldsen.player.domain.events;

import com.kjeldsen.domain.Event;
import com.kjeldsen.player.domain.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@ToString
@Document(collection = "PlayerFallOfCliffEvents")
@TypeAlias("PlayerFallOfCliffEvent")
public class PlayerFallOffCliffEvent extends Event {
    private Player.PlayerId playerId;
}
