package com.kjeldsen.match.engine.selection;

import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.state.GameStateException;
import com.kjeldsen.match.models.Player;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
    public static Player select(GameState state, Player initiator) {
        PitchArea ballArea = state.getBallState().getArea();
        return switch (ballArea.rank()) {
            case BACK -> selectFromBack(state, initiator);
            case MIDDLE -> selectFromMidfield(state, initiator);
            case FORWARD -> selectFromForward(state, initiator);
        };
    }

    public static Player selectFromBack(GameState state, Player initiator) {
        // Currently select any midfielder nearby to move the ball forward along the pitch
        return nearbyTeammates(state, initiator)
            .filter(player -> player.getPosition().isMidfielder())
            .findAny()
            .orElseThrow(
                () -> new GameStateException(state, "No midfielders found to receive ball"));
    }

    public static Player selectFromMidfield(GameState state, Player initiator) {
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
                    double factor = state.attackingTeam().getTactic().selectionFactor(position);

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
                    return (int) (value * factor);
                },
                (a, b) -> b, HashMap::new));

        return Probability.drawPlayer(state, candidates, values);
    }

    public static Player selectFromForward(GameState state, Player initiator) {
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

    public static Optional<Player> selectForward(GameState state, Player initiator) {
        return nearbyTeammates(state, initiator)
            .filter(player -> player.getPosition().isForward())
            .findAny();
    }

    public static Optional<Player> selectFromArea(
        GameState state, Player initiator, PitchArea area) {

        return state.attackingTeam().getPlayers().stream()
            .filter(teammate -> !Objects.equals(initiator.getId(), teammate.getId()))
            .filter(teammate ->
                teammate.getPosition().coverage().stream()
                    .anyMatch(teammateArea -> teammateArea == area))
            .findAny();
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
