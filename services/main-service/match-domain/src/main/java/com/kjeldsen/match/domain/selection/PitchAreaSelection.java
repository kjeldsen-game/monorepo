package com.kjeldsen.match.domain.selection;

import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.state.BallState;
import com.kjeldsen.player.domain.PitchArea;

import java.util.*;
import java.util.stream.Collectors;

public class PitchAreaSelection {

    /*
     * Players can cover more than one pitch area based on their position, but a definite area must
     * be assumed by each player when they are involved in a duel. From a list of possible pitch
     * area they are allowed to play in, this class selects a single pitch area for each player.
     * Also, if the ball is in midfield, it cannot go back.
     */
    public static Optional<PitchArea> select(PitchArea ballArea, Player player, Boolean nearbyOnly) {
        List<PitchArea> areas = player.getPosition().coverage().stream()
                .filter(coverage -> !nearbyOnly || coverage.isNearby(ballArea))
                .filter(coverage -> !PitchArea.PitchRank.MIDDLE.equals(ballArea.rank()) || !PitchArea.PitchRank.BACK.equals(coverage.rank()))
                .toList();

        if (!areas.isEmpty()) {
            return Optional.of(areas.get(new Random().nextInt(areas.size())));
        }
        return Optional.empty();
    }


    /*
     * Players can cover more than one pitch area based on their position, but a definite area must
     * be assumed by each player when they are involved in a duel. From a list of possible pitch
     * area they are allowed to play in, this class selects a single pitch area for each player.
     * Also, if the ball is in midfield, it cannot go back.
     *
     * If the same player is changing area, then a new valid area independent from coverage is taken.
     */
    public static Optional<PitchArea> select(BallState ballState, Player player, Boolean nearbyOnly) {

        PitchArea ballArea = ballState.getArea();
        Player ballPlayer = ballState.getPlayer();
        boolean ballChangesPlayer = player != null && !ballPlayer.equals(player);

        List<PitchArea> areas = new ArrayList<>();

        if (ballChangesPlayer) {
            // If ball is changing from one player to another, the area is selected from the target player coverage.
            if (ballState.getArea().equals(PitchArea.OUT_OF_BOUNDS)) {
                areas = List.of(PitchArea.midfield());
            } else {
                areas = player.getPosition().coverage().stream()
                    .filter(coveredArea -> !nearbyOnly || coveredArea.isNearby(ballArea))
                    .filter(coveredArea -> !PitchArea.PitchRank.MIDDLE.equals(ballArea.rank()) || !PitchArea.PitchRank.BACK.equals(coveredArea.rank()))
                    .toList();
            }
        } else {
            // If a player is moving himself to another area, then we break from the player normal coverage.
            List<PitchArea> allAreas = Arrays.stream(PitchArea.values())
                .filter(area -> area != PitchArea.OUT_OF_BOUNDS)
                .toList();

            areas = allAreas.stream()
                    .filter(area -> !nearbyOnly || area.isNearby(ballArea))
                    .filter(area -> PitchArea.PitchRank.MIDDLE.equals(ballArea.rank()) ? !PitchArea.PitchRank.BACK.equals(area.rank()) : true)
                    .filter(area -> PitchArea.PitchRank.FORWARD.equals(ballArea.rank()) ? !(PitchArea.PitchRank.BACK.equals(area.rank()) || PitchArea.PitchRank.MIDDLE.equals(area.rank())): true)
                    .toList();
        }

        // Select random area from valid areas.
        if (!areas.isEmpty()) {
            return Optional.of(areas.get(new Random().nextInt(areas.size())));
        } else {
            System.out.println("No area to select from.");
        }

        return Optional.empty();

    }
}
