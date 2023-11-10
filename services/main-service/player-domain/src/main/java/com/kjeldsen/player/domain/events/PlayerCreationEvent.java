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
    private Integer age;
    private PlayerPosition position;
    // TODO 72-add-potentials-to-the-player change Integer for a wrapper object to save moved current points and potential points
    private Map<PlayerSkill, PlayerSkills> initialSkills;
    private Team.TeamId teamId;
    private PlayerCategory playerCategory;

}
