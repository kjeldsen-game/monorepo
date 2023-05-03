package com.kjeldsen.player.domain.events;

import com.kjeldsen.events.Event;
import com.kjeldsen.player.domain.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@Document(collection = "PlayerTrainingBloomEvents")
@TypeAlias("PlayerTrainingBloomEvent")
public class PlayerTrainingBloomEvent extends Event {

    private Player.PlayerId playerId;
    private int yearsOn;
    private int bloomStartAge;
    private int bloomSpeed;

}
