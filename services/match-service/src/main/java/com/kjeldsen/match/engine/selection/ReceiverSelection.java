package com.kjeldsen.match.engine.selection;

import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.models.Player;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
                () -> new GameStateException(state, "No midfielders found to receive ball"));
    }

    public static Player selectReceiverFromMidfield(GameState state, Player initiator) {
        List<Player> candidates = nearbyTeammates(state, initiator)
            .filter(
                player -> player.getPosition().isMidfielder() || player.getPosition().isWingback())
            .toList();

        if (candidates.isEmpty()) {
            throw new GameStateException(state, "No forwards or midfielders found to receive ball");
        }

        // When calculating probabilities, first assign values based on the player's position
        // category (a natural position counts as 2, an offensive as 4 and a support as 1).
        Map<Long, Integer> values = candidates.stream()
            .collect(Collectors.toMap(
                Player::getId,
                player -> {
                    PlayerPosition position = player.getPosition();
                    double modifier = state.attackingTeam().getTactic().selectionBonus(position);

                    int value;
                    if (position.isNatural()) {
                        value = 2;
                    } else if (position.isDefensive()) {
                        value = 1;
                    } else if (position.isOffensive()) {
                        value = 4;
                    } else {
                        value = 0;
                    }
                    return (int) (value * (modifier + 1));
                },
                (a, b) -> b, HashMap::new));

        return Probability.drawPlayer(state, candidates, values);
    }

    public static Player selectReceiverFromForward(GameState state, Player initiator) {
        List<Player> candidates = nearbyTeammates(state, initiator)
            .filter(
                player -> player.getPosition().isForward()
                    || (player.getPosition().isMidfielder() && player.getPosition().isOffensive()))
            .toList();

        if (candidates.isEmpty()) {
            throw new GameStateException(state, "No players found to receive ball from forward");
        }

        // When calculating probabilities, first assign values based on the player's position
        // category (a natural position counts as 2, an offensive as 1).
        Map<Long, Integer> values = candidates.stream()
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

        return Probability.drawPlayer(state, candidates, values);
    }

    private static Stream<Player> nearbyTeammates(GameState state, Player player) {
        return state.attackingTeam().getPlayers().stream()
            .filter(teammate -> !Objects.equals(player.getId(), teammate.getId()))
            .filter(teammate -> teammateIsNearby(state.getBallState().getArea(), teammate));
    }

    private static boolean teammateIsNearby(PitchArea playerArea, Player teammate) {
        return teammate.getPosition().coverage().stream()
            .anyMatch(teammateArea -> teammateArea.teammateIsNearby(playerArea));
    }
}
