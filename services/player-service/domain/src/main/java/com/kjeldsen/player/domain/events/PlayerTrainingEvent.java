package com.kjeldsen.player.domain.events;

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
    private PlayerDeclineEvent decline;
    private int points;


}
