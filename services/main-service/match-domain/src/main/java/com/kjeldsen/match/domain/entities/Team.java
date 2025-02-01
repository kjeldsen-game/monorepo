package com.kjeldsen.match.domain.entities;

import com.kjeldsen.match.domain.modifers.HorizontalPressure;
import com.kjeldsen.match.domain.modifers.Tactic;
import com.kjeldsen.match.domain.modifers.TeamModifiers;
import com.kjeldsen.match.domain.modifers.VerticalPressure;
import com.kjeldsen.match.domain.utils.JsonUtils;
import com.kjeldsen.player.domain.PlayerPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    String id;
    TeamRole role;
    List<Player> players;
    List<Player> bench;
    Boolean specificLineup;

    Integer rating;

    TeamModifiers modifiers;

    // TODO remove this if modifiers reworked
    Tactic tactic;
    VerticalPressure verticalPressure;
    HorizontalPressure horizontalPressure;

    public List<Player> getPlayers(PlayerPosition position) {
        return this.players.stream().filter(p -> position.equals(p.getPosition())).toList();
    }

    @Override
    public String toString() {
        return JsonUtils.prettyPrint(this);
    }
}
