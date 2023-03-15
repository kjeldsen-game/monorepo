package com.kjeldsen.player.domain.events;

import com.kjeldsen.player.domain.PlayerDecline;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class PlayerTrainingEvent extends Event {

    private PlayerId playerId;
    private PlayerSkill skill;
    private PlayerBloomEvent bloom;
    private PlayerDecline decline;
    private int points;


}
