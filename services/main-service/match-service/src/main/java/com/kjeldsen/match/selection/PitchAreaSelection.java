package com.kjeldsen.match.selection;

import com.kjeldsen.match.entities.Player;
import com.kjeldsen.player.domain.PitchArea;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class PitchAreaSelection {

    /*
     * Players can cover more than one pitch area based on their position, but a definite area must
     * be assumed by each player when they are involved in a duel. From a list of possible pitch
     * area they are allowed to play in, this class selects a single pitch area for each player.
     * Also, if the ball is in midfield, it cannot go back.
     */
    public static Optional<PitchArea> select(PitchArea ballArea, Player player, Boolean nearbyOnly) {
        List<PitchArea> areas = player.getPosition().coverage().stream()
            .filter(coverage -> !nearbyOnly || coverage.teammateIsNearby(ballArea))
            .filter(coverage -> !PitchArea.PitchRank.MIDDLE.equals(ballArea.rank()) || !PitchArea.PitchRank.BACK.equals(coverage.rank()))
            .toList();

        if (!areas.isEmpty()) {
            return Optional.of(areas.get(new Random().nextInt(areas.size())));
        }
        return Optional.empty();
    }
}
