package com.kjeldsen.player.domain.events;

import com.kjeldsen.domain.Event;
import com.kjeldsen.player.domain.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@SuperBuilder
@Document(collection = "PlayerCreationEvent")
@TypeAlias("PlayerCreationEvent")
public class PlayerCreationEvent extends Event {

    private Player.PlayerId playerId;
    private String name;
    private PlayerAge age;
    private PlayerPosition position;
    private Map<PlayerSkill, PlayerSkills> initialSkills;
    private Team.TeamId teamId;
    private PlayerCategory playerCategory;

}
