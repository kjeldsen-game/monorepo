package com.kjeldsen.match.engine.selection;

import static com.kjeldsen.match.engine.selection.PlayerSelection.selectFromProbabilities;
import static com.kjeldsen.match.engine.selection.PlayerSelection.toProbabilityMap;

import com.kjeldsen.match.domain.aggregate.Player;
import com.kjeldsen.match.domain.id.PlayerId;
import com.kjeldsen.match.domain.type.PitchArea;
import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.state.GameState;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReceiverSelection {

    /*
     * Selects a player to receive the ball from a teammate.
     *
     * For achieving the concept of an 'opportunity', in which a midfielder or forward receives the
     * ball to begin an attack (ending in either a goal or losing the ball), here we can select the
     * opportunity starter to receive the ball by incorporating rules and modifiers about, for
     * example, which players are better at generating opportunities.
     *
     * Any other product requirements about opportunities can also be implemented here by modifying
     * how ball receivers are selected at the start of an attack.
     */

    // Returns a player to receive the ball based on the current pitch area.
    public static Player selectReceiver(GameState state, Player initiator) {
        PitchArea ballArea = state.getBallState().getArea();

        return switch (ballArea) {
            case BACK -> selectReceiverFromBack(state.getAttackingTeam().getPlayers(), initiator);
            case MIDFIELD ->
                selectReceiverFromMidfield(state.getAttackingTeam().getPlayers(), initiator);
            case FORWARD ->
                selectReceiverFromForward(state.getAttackingTeam().getPlayers(), initiator);
        };
    }

    public static Player selectReceiverFromBack(List<Player> players, Player initiator) {
        // For now select any midfielder to move the ball forward along the pitch
        return players.stream()
            .filter(p -> p.getId() != initiator.getId())
            .filter(p -> p.getPosition().isMidfielder())
            .findAny()
            .orElseThrow(
                () -> new GameStateException("No midfielders found to receive ball"));
    }

    public static Player selectReceiverFromMidfield(List<Player> players, Player initiator) {
        // In midfield, only midfielders or wingbacks are valid candidates for receiving the ball
        List<Player> candidates = players.stream()
            .filter(p -> p.getId() != initiator.getId())
            .filter(p -> p.getPosition().isMidfielder() || p.getPosition().isWingback())
            .toList();

        if (candidates.isEmpty()) {
            throw new GameStateException("No forwards or midfielders found to receive ball");
        }

        // When calculating probabilities, first assign values based on the player's position
        // category (a natural position counts as 2, an offensive as 4 and a support as 1).
        Map<PlayerId, Integer> values = candidates.stream()
            .collect(Collectors.toMap(
                Player::getId,
                player -> {
                    if (player.getPosition().isNatural()) {
                        return 2;
                    } else if (player.getPosition().isDefensive()) {
                        return 1;
                    } else if (player.getPosition().isOffensive()) {
                        return 4;
                    } else {
                        return 0;
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

    public static Player selectReceiverFromForward(List<Player> players, Player initiator) {
        List<Player> candidates = players.stream()
            .filter(p -> p.getId() != initiator.getId())
            .filter(
                p -> p.getPosition().isForward()
                    || (p.getPosition().isMidfielder() && p.getPosition().isOffensive()))
            .toList();

        if (players.isEmpty()) {
            throw new GameStateException("No players found to receive ball from forward");
        }

        // When calculating probabilities, first assign values based on the player's position
        // category (a natural position counts as 2, an offensive as 1).
        Map<PlayerId, Integer> values = candidates.stream()
            .collect(Collectors.toMap(
                Player::getId,
                player -> {
                    if (player.getPosition().isForward()) {
                        return 2;
                    } else {
                        // At this stage the player must be a midfielder and offensive
                        return 1;
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

}
