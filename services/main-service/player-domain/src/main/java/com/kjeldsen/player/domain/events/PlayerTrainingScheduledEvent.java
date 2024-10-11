package com.kjeldsen.player.domain.events;

import com.kjeldsen.domain.Event;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@Document(collection = "PlayerTrainingScheduledEvent")
@TypeAlias("PlayerTrainingScheduledEvent")
@NoArgsConstructor
public class PlayerTrainingScheduledEvent extends Event {

    private Player.PlayerId playerId;
    private PlayerSkill skill;
    private Integer trainingDays; // TODO only for simulation
    private Instant startDate;  // TODO remove in future not needed
    private Instant endDate; // TODO remove in future not needed end date
    private Status status;

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
