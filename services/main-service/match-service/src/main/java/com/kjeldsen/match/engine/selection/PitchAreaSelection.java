package com.kjeldsen.match.engine.selection;

import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.models.Player;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class PitchAreaSelection {

    /*
     * Players can cover more than one pitch area based on their position, but a definite area must
     * be assumed by each player when they are involved in a duel. From a list of possible pitch
     * area they are allowed to play in, this class selects a single pitch area for each player.
     */

    public static Optional<PitchArea> select(PitchArea ballArea, Player player) {
        List<PitchArea> areas = player.getPosition().coverage().stream()
            .filter(ballArea::teammateIsNearby)
            .toList();

        if (!areas.isEmpty()) {
            return Optional.of(areas.get(new Random().nextInt(areas.size())));
        }
        return Optional.empty();
    }
}
