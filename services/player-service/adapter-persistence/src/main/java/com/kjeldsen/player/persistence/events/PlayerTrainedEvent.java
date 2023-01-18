package com.kjeldsen.player.persistence.events;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class PlayerTrainedEvent extends Event {

    private PlayerId playerId;
    private PlayerSkill skill;
    private int points;

}
