package com.kjeldsen.player.domain.events;

import com.kjeldsen.domain.Event;
import com.kjeldsen.player.domain.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "PlayerTrainingBloomEvents")
@TypeAlias("PlayerTrainingBloomEvent")
public class PlayerTrainingBloomEvent extends Event {

    private Player.PlayerId playerId;
    private int yearsOn;
    private int bloomStartAge;
    private int bloomSpeed;

}
