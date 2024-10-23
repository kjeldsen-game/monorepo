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
@Document(collection = "FansEvent")
@TypeAlias("FansEvent")
public class FansEvent extends Event {

    private Team.TeamId teamId;
    private Integer fans;
    private Team.Fans.ImpactType fansImpactType;
}
