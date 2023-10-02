package com.kjeldsen.match.engine.selection;

import static com.kjeldsen.match.engine.selection.PlayerSelection.selectFromProbabilities;
import static com.kjeldsen.match.engine.selection.PlayerSelection.toProbabilityMap;

import com.kjeldsen.match.domain.aggregate.Player;
import com.kjeldsen.match.domain.id.PlayerId;
import com.kjeldsen.match.engine.exceptions.GameStateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefenderSelection {

    /*
     * Selects a defender to challenge an attacker in a duel.
     */

    // Returns a defender for an attack in the midfield area
    public static Player selectDefenderForMidfield(List<Player> players) {
        List<Player> candidates = players.stream()
            .filter(
                p -> (p.getPosition().isMidfielder() && !p.getPosition().isOffensive())
                    || p.getPosition().isWingback())
            .toList();

        if (candidates.isEmpty()) {
            throw new GameStateException("No defenders found to defend in midfield");
        }

        // Center: Offensive counts as 0, Natural as 1 and Defensive as 2
        // Sides: Winger counts as 0, Natural as 1, Defensive as 1, wingback as 2, Fullback as 3
        Map<PlayerId, Integer> values = candidates.stream()
            .collect(Collectors.toMap(
                Player::getId,
                player -> {
                    if (player.getPosition().isCentral()) {
                        if (player.getPosition().isNatural()) {
                            return 1;
                        } else if (player.getPosition().isDefensive()) {
                            return 2;
                        } else {
                            return 0;
                        }
                    } else {
                        // On sides
                        if (player.getPosition().isNatural()) {
                            return 1;
                        } else if (player.getPosition().isDefensive()) {
                            return 2;
                        } else if (player.getPosition().isFullBack()) {
                            return 3;
                        } else {
                            return 0;
                        }
                    }
                },
                (a, b) -> b, HashMap::new));

        Map<PlayerId, Double> probabilities = toProbabilityMap(values);
        PlayerId selectedPlayerId = selectFromProbabilities(probabilities);
        return candidates.stream()
            .filter(p -> p.getId() == selectedPlayerId)
            .findAny()
            .orElseThrow(() -> new GameStateException("No player found")); // Should never fail!
    }

    // Returns a defender for an attack in the forward area
    public static Player selectDefenderForForward(List<Player> players) {
        // If in the penalty box, only CB can defend the ball.
        return null;
    }
}
