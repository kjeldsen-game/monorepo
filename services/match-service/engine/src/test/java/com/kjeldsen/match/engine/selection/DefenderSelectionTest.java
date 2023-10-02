package com.kjeldsen.match.engine.selection;

import static com.kjeldsen.match.engine.selection.DefenderSelection.selectDefenderForMidfield;
import static com.kjeldsen.match.engine.utils.RandomHelper.genPlayerWithPosition;

import com.kjeldsen.match.domain.aggregate.Player;
import com.kjeldsen.match.domain.id.TeamId;
import com.kjeldsen.match.domain.type.PlayerPosition;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class DefenderSelectionTest {


    // This test recreates an example from the specification of player selection.
    // We select a player to defend against an attack in the midfield area.
    // The probabilities for the five players to be selected are:
    // Center midfielder = 25%
    // Offensive midfielder = 0%
    // Defensive midfielder = 50%
    // Right midfielder = 25%
    // Left winger = 0%
    // After running many simulations we should see these numbers approximately.
    @Test
    void selectMidfieldReceiverDistribution() {
        TeamId teamId = TeamId.generate();
        Player initiator = genPlayerWithPosition(teamId, PlayerPosition.CENTER_MIDFIELDER);

        List<Player> players = List.of(
            genPlayerWithPosition(teamId, PlayerPosition.CENTER_MIDFIELDER),
            genPlayerWithPosition(teamId, PlayerPosition.OFFENSIVE_MIDFIELDER),
            genPlayerWithPosition(teamId, PlayerPosition.DEFENSIVE_MIDFIELDER),
            genPlayerWithPosition(teamId, PlayerPosition.RIGHT_MIDFIELDER),
            genPlayerWithPosition(teamId, PlayerPosition.LEFT_WINGER));

        Map<PlayerPosition, Integer> selections = new HashMap<>();
        selections.put(PlayerPosition.CENTER_MIDFIELDER, 0);
        selections.put(PlayerPosition.OFFENSIVE_MIDFIELDER, 0);
        selections.put(PlayerPosition.DEFENSIVE_MIDFIELDER, 0);
        selections.put(PlayerPosition.RIGHT_MIDFIELDER, 0);
        selections.put(PlayerPosition.LEFT_WINGER, 0);

        // Make this number larger to get more accurate results
        for (int i = 0; i < 10000; i++) {
            Player selected = selectDefenderForMidfield(players);
            selections.merge(selected.getPosition(), 1, Integer::sum);
        }

        System.out.println(selections);
    }

}
