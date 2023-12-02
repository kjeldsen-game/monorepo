package com.kjeldsen.player.domain.events;

import com.kjeldsen.domain.Event;
import com.kjeldsen.player.domain.Player;
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
@Document(collection = "PlayerPotentialRiseScheduledEvent")
@TypeAlias("PlayerPotentialRiseScheduledEvent")
@NoArgsConstructor
public class PlayerPotentialRiseScheduledEvent extends Event {

    private Player.PlayerId playerId;
    private Integer daysToSimulate;
    private Instant startDate;
    private Instant endDate;

}
