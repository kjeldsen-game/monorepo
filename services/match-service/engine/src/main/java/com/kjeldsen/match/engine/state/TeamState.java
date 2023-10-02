package com.kjeldsen.match.engine.state;

import com.kjeldsen.match.domain.aggregate.Player;
import com.kjeldsen.match.domain.aggregate.Team;
import com.kjeldsen.match.domain.id.PlayerId;
import com.kjeldsen.match.domain.type.Card;
import com.kjeldsen.match.domain.type.PitchArea;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TeamState {

    /*
     * Represents positions and conditions of players on the team
     */

    List<Player> players;
    Map<PlayerId, PitchArea> playerLocation;
    Map<PlayerId, Card> penaltyCards;
    Map<PlayerId, PlayerId> substitutions;
    int score;
    int fouls;
    int injuries;

    public static TeamState init(Team team) {
        // In later version - assign players to pitch areas and keep track of where they are
        Map<PlayerId, PitchArea> locations =
            team.getPlayers().stream()
                .collect(Collectors.toMap(Player::getId, p -> PitchArea.MIDFIELD));

        Map<PlayerId, Card> penaltyCards =
            team.getPlayers().stream()
                .collect(Collectors.toMap(Player::getId, p -> Card.NONE));

        Map<PlayerId, PlayerId> substitutions = new HashMap<>();

        return TeamState.builder()
            .players(team.getPlayers())
            .playerLocation(locations)
            .penaltyCards(penaltyCards)
            .substitutions(substitutions)
            .score(0)
            .fouls(0)
            .injuries(0)
            .build();
    }
}
