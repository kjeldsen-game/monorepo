package com.kjeldsen.player.domain.events;

import com.kjeldsen.events.domain.Event;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
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
@Document(collection = "PlayerPotentialRaiseEvents")
@TypeAlias("PlayerPotentialRaiseEvent")
public class PlayerPotentialRiseEvent extends Event {
    private Player.PlayerId playerId;
    private int potentialBeforeRaise;
    private int pointsToRise;
    private int potentialAfterRaise;
    private PlayerSkill skillThatRisen;

}
