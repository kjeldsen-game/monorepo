package com.kjeldsen.player.domain.models.training;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.Team;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Builder(toBuilder = true)
@Document(collection = "TrainingEvents")
@TypeAlias("TrainingEvent")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrainingEvent {
    private String reference;

    private Player player;
    private Team.TeamId teamId;
    private PlayerSkill skill;
    private TrainingType type;
    private Instant occurredAt;

    private Integer points;
    private Integer pointsBeforeTraining;
    private Integer pointsAfterTraining;

    // Optional fields
    private Integer currentDay;
    private TrainingModifier modifier;


    public enum TrainingModifier {
        BLOOM,
        FALL_OFF_CLIFF
    }
    public enum TrainingType {
        PLAYER_TRAINING,
        POTENTIAL_RISE,
        DECLINE_TRAINING
    }
}
