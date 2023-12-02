package com.kjeldsen.player.domain.events;

import com.kjeldsen.domain.Event;
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
    private Integer actualPoints;
    private Integer currentDay;
    private Integer potentialBeforeRaise;
    private Integer pointsToRise;
    private Integer potentialAfterRaise;
    private PlayerSkill skillThatRisen;
}
