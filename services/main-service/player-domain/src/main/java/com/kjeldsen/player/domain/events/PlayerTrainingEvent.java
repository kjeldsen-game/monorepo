package com.kjeldsen.player.domain.events;

import com.kjeldsen.domain.Event;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.Team;
import lombok.*;
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

    private String scheduledTrainingId;
    private Player.PlayerId playerId;
    private Team.TeamId teamId;
    private PlayerSkill skill;
    @Builder.Default
    private boolean isBloom = false;
    private Integer points;
    private Integer actualPoints;
    private Integer potentialPoints;
    private Integer pointsBeforeTraining;
    private Integer pointsAfterTraining;
    private Integer currentDay;

}
