package com.kjeldsen.match.engine.selection;

import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.entities.Id;
import com.kjeldsen.match.entities.PitchArea;
import com.kjeldsen.match.entities.player.Player;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return switch (ballArea.rank()) {
            case BACK -> selectReceiverFromBack(state, initiator);
            case MIDDLE -> selectReceiverFromMidfield(state, initiator);
            case FORWARD -> selectReceiverFromForward(state, initiator);
        };
    }

    public static Player selectReceiverFromBack(GameState state, Player initiator) {
        // Currently select any midfielder nearby to move the ball forward along the pitch
        return nearbyTeammates(state, initiator)
            .filter(player -> player.getPosition().isMidfielder())
            .findAny()
            .orElseThrow(
                () -> new GameStateException("No midfielders found to receive ball"));
    }

    public static Player selectReceiverFromMidfield(GameState state, Player initiator) {
        List<Player> candidates = nearbyTeammates(state, initiator)
            .filter(
                player -> player.getPosition().isMidfielder() || player.getPosition().isWingback())
            .toList();

        if (candidates.isEmpty()) {
            throw new GameStateException("No forwards or midfielders found to receive ball");
        }

        // When calculating probabilities, first assign values based on the player's position
        // category (a natural position counts as 2, an offensive as 4 and a support as 1).
        Map<Id, Integer> values = candidates.stream()
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

        Map<Id, Double> probabilities = Probability.toProbabilityMap(values);

        Id selectedPlayerId = Probability.selectFromProbabilities(probabilities);
        return candidates.stream()
            .filter(p -> p.getId() == selectedPlayerId)
            .findAny()
            .orElseThrow(() -> new GameStateException("No receiver found")); // Should never fail!
    }

    public static Player selectReceiverFromForward(GameState state, Player initiator) {
        List<Player> candidates = nearbyTeammates(state, initiator)
            .filter(
                player -> player.getPosition().isForward()
                    || (player.getPosition().isMidfielder() && player.getPosition().isOffensive()))
            .toList();

        if (candidates.isEmpty()) {
            throw new GameStateException("No players found to receive ball from forward");
        }

        // When calculating probabilities, first assign values based on the player's position
        // category (a natural position counts as 2, an offensive as 1).
        Map<Id, Integer> values = candidates.stream()
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

        Map<Id, Double> probabilities = Probability.toProbabilityMap(values);
        Id selectedPlayerId = Probability.selectFromProbabilities(probabilities);
        return candidates.stream()
            .filter(p -> p.getId() == selectedPlayerId)
            .findAny()
            .orElseThrow(() -> new GameStateException("No receiver found")); // Should never fail!
    }

    private static Stream<Player> nearbyTeammates(GameState state, Player player) {
        return state.attackingTeam().getPlayers().stream()
            .filter(teammate -> player.getId() != teammate.getId())
            .filter(teammate -> teammateIsNearby(state.getBallState().getArea(), teammate));
    }

    private static boolean teammateIsNearby(PitchArea playerArea, Player teammate) {
        return teammate.getPosition().coverage().stream()
            .anyMatch(teammateArea -> teammateArea.teammateIsNearby(playerArea));
    }
}
