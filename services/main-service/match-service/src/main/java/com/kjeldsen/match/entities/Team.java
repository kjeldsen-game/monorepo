package com.kjeldsen.match.entities;

import com.kjeldsen.match.modifers.HorizontalPressure;
import com.kjeldsen.match.modifers.Tactic;
import com.kjeldsen.match.modifers.VerticalPressure;
import com.kjeldsen.match.utils.JsonUtils;
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

    Integer rating;

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
