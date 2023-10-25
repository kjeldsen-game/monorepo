package com.kjeldsen.match.engine.selection;

import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.entities.Id;
import com.kjeldsen.match.entities.PitchArea;
import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.entities.player.Player;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefenderSelection {

    /*
     * Selects a defender to challenge an attacker in a positional duel.
     */

    // Returns a defender for an attack in the midfield area
    public static Player selectDefenderForMidfield(GameState state) {
        List<Player> players = state.defendingTeam().getPlayers();
        PitchArea area = state.getBallState().getArea();
        List<Player> candidates = players.stream()
            .filter(
                p -> (p.getPosition().isMidfielder() && !p.getPosition().isOffensive())
                    || p.getPosition().isWingback())
            .filter(
                p -> p.getPosition().coverage().stream().anyMatch(area::opponentIsNearby))
            .filter(p -> isFree(state, p))
            .toList();

        if (candidates.isEmpty()) {
            throw new GameStateException("No defenders found to defend in midfield attack");
        }

        Map<Id, Integer> values = candidates.stream()
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

        Map<Id, Double> probabilities = Probability.toProbabilityMap(values);
        Id selectedId = Probability.selectFromProbabilities(probabilities);
        return candidates.stream()
            .filter(p -> p.getId() == selectedId)
            .findAny()
            .orElseThrow(() -> new GameStateException("No player found"));
    }

    // Returns a defender for an attack in the forward area
    public static Player selectDefenderForForward(GameState state) {
        List<Player> players = state.defendingTeam().getPlayers();
        PitchArea area = state.getBallState().getArea();
        List<Player> candidates = players.stream()
            .filter(defender -> defender.getPosition().isDefender())
            .filter(candidate ->
                candidate.getPosition().coverage().stream().anyMatch(area::opponentIsNearby))
            .filter(candidate -> isFree(state, candidate))
            .toList();

        if (candidates.isEmpty()) {
            throw new GameStateException("No defenders found to defend in forward area attack");
        }

        Map<Id, Integer> values = candidates.stream()
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

        Map<Id, Double> probabilities = Probability.toProbabilityMap(values);
        Id selectedId = Probability.selectFromProbabilities(probabilities);
        return candidates.stream()
            .filter(p -> p.getId() == selectedId)
            .findAny()
            .orElseThrow(() -> new GameStateException("No player found"));
    }

    // Returns true if the player was *not* involved as a defender in the previous positional duel.
    // This is to avoid players taking part in consecutive plays.
    private static boolean isFree(GameState state, Player player) {
        return state.getPlays().stream()
            .sorted(Comparator.comparingInt(Play::getMinute).reversed())
            .map(Play::getDuel)
            .filter(duel -> duel.getType() == DuelType.POSITIONAL
                && duel.getChallenger().getTeamId() == player.getTeamId())
            .findFirst()
            .map(duel -> duel.getChallenger().getId() != player.getId())
            .orElse(true);
    }
}