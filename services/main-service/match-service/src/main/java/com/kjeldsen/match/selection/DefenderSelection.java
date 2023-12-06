package com.kjeldsen.match.selection;

import com.kjeldsen.match.entities.Play;
import com.kjeldsen.match.entities.duel.DuelType;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.player.domain.PitchArea;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefenderSelection {

    /*
     * Selects a defender to challenge an attacker in a positional duel.
     */

    // Returns a defender for an attack in the midfield area
    public static Player selectFromMidfield(GameState state, PitchArea pitchArea) {
        List<Player> players = state.defendingTeam().getPlayers();
        List<Player> candidates = players.stream()
            .filter(candidate ->
                (candidate.getPosition().isMidfielder() && !candidate.getPosition().isOffensive())
                    || candidate.getPosition().isWingback())
            .filter(candidate -> candidate.getPosition().coverage().contains(pitchArea))
            .filter(candidate -> isFree(state, candidate))
            .toList();

        if (candidates.isEmpty()) {
            return null;
        }

        Map<String, Integer> values = candidates.stream()
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

        return Probability.drawPlayer(state, candidates, values);
    }

    // Returns a defender for an attack in the forward area
    public static Player selectFromBack(GameState state, PitchArea pitchArea) {
        List<Player> players = state.defendingTeam().getPlayers();
        List<Player> candidates = players.stream()
            .filter(candidate -> candidate.getPosition().isDefender())
            .filter(candidate ->
                candidate.getPosition().coverage().contains(pitchArea))
            .filter(candidate -> isFree(state, candidate))
            .toList();

        if (candidates.isEmpty()) {
            return null;
        }

        Map<String, Integer> values = candidates.stream()
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

        return Probability.drawPlayer(state, candidates, values);
    }

    // Returns true if the player was *not* involved as a defender in the previous positional duel.
    // This is to avoid players taking part in consecutive plays.
    private static boolean isFree(GameState state, Player player) {
        return state.getPlays().stream()
            .sorted(Comparator.comparingInt(Play::getClock).reversed())
            .map(Play::getDuel)
            .filter(duel -> duel.getType() == DuelType.POSITIONAL
                && duel.getChallenger() != null
                && Objects.equals(duel.getChallenger().getTeamId(), player.getTeamId()))
            .findFirst()
            .map(duel -> !Objects.equals(duel.getChallenger().getId(), player.getId()))
            .orElse(true);
    }
}