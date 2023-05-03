package com.kjeldsen.player.domain.events;

import com.kjeldsen.events.Event;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.Team;
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
    private Map<PlayerSkill, Integer> initialSkills;
    private Team.TeamId teamId;

}
