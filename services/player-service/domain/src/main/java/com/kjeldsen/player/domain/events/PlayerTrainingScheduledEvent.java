package com.kjeldsen.player.domain.events;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Document(collection = "PlayerTrainingScheduledEvent")
@TypeAlias("PlayerTrainingScheduledEvent")
public class PlayerTrainingScheduledEvent extends Event {

    private PlayerId playerId;
    private Set<PlayerSkill> skills; // TODO scheduled individual by skills - so API can send multiple skills to train for simplicity but BE generated per each
    private Integer trainingDays;
    private Instant startDate;
    private Instant endDate;

}
