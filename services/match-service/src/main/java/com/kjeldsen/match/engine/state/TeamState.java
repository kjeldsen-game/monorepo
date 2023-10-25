package com.kjeldsen.match.engine.state;

import com.kjeldsen.match.entities.Card;
import com.kjeldsen.match.entities.Id;
import com.kjeldsen.match.entities.PitchArea;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.entities.player.Player;
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
    Map<Id, PitchArea> playerLocation;
    Map<Id, Card> penaltyCards;
    int score;
    int fouls;
    int injuries;

    public static TeamState init(Team team) {
        // In later version - assign players to pitch areas and keep track of where they are
        Map<Id, PitchArea> locations =
            team.getPlayers().stream()
                .collect(Collectors.toMap(Player::getId, p -> PitchArea.CENTER_MIDFIELD));

        Map<Id, Card> penaltyCards =
            team.getPlayers().stream()
                .collect(Collectors.toMap(Player::getId, p -> Card.NONE));

        return TeamState.builder()
            .players(team.getPlayers())
            .playerLocation(locations)
            .penaltyCards(penaltyCards)
            .score(0)
            .fouls(0)
            .injuries(0)
            .build();
    }
}
