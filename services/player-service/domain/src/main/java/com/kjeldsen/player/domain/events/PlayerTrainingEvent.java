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
@Document(collection = "PlayerTrainingEvents")
@TypeAlias("PlayerTrainingEvent")
@AllArgsConstructor
@NoArgsConstructor
public class PlayerTrainingEvent extends Event {

    private Player.PlayerId playerId;
    private PlayerSkill skill;
    private PlayerTrainingBloomEvent bloom;
    private Integer points;
    private Integer pointsBeforeTraining;
    private Integer pointsAfterTraining;
    private Integer currentDay;

}
