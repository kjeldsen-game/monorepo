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

    String name;
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

    public Team deepCopy() {
        return Team.builder()
            .name(this.name)
            .id(this.id)
            .role(this.role)
            .players(this.players != null
                ? this.players.stream().map(Player::deepCopy).toList()
                : null)
            .bench(this.bench != null
                ? this.bench.stream().map(Player::deepCopy).toList()
                : null)
            .specificLineup(this.specificLineup)
            .rating(this.rating)
            .modifiers(this.modifiers != null ? this.modifiers.deepCopy() : null)
            .build();
    }

    public void cleanPlayers() {
        this.players = null;
        this.bench = null;
    }
}
