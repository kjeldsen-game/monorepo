package com.kjeldsen.player.domain.events;

import com.kjeldsen.domain.Event;
import com.kjeldsen.player.domain.Team;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@Document(collection = "CanteraInvestmentEvent")
@TypeAlias("CanteraInvestmentEvent")
public class CanteraInvestmentEvent extends Event {

    private Team.TeamId teamId;
    private Team.Cantera.Investment investment;
    private Integer points;

}
