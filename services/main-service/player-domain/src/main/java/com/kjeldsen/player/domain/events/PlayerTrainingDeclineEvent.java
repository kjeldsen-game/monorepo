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
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Document(collection = "PlayerTrainingDeclineEvents")
@TypeAlias("PlayerTrainingDeclineEvent")
public class PlayerTrainingDeclineEvent extends Event {

    private Player.PlayerId playerId;
    private Integer declineStartAge;
    private Integer declineSpeed;
    private PlayerSkill skill;
    private Integer pointsToSubtract;
    private Integer pointsBeforeTraining;
    private Integer pointsAfterTraining;
    private Integer currentDay;

}
