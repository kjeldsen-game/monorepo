package com.kjeldsen.player.domain.events;

import com.kjeldsen.events.Event;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@Document(collection = "PlayerTrainingEvents")
@TypeAlias("PlayerTrainingEvent")
public class PlayerTrainingEvent extends Event {

    private PlayerId playerId;
    private PlayerSkill skill;
    private PlayerTrainingBloomEvent bloom;
    private Integer points;
    private Integer pointsBeforeTraining;
    private Integer pointsAfterTraining;
    private Integer currentDay;

}
