package com.kjeldsen.player.domain.events;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PlayerTrainingEvent extends Event {

    private PlayerId playerId;
    private PlayerSkill skill;
    private int points;

}
