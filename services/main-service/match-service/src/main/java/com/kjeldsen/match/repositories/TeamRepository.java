package com.kjeldsen.match.repositories;


import com.kjeldsen.match.models.Team;
import java.util.Optional;

public class TeamRepository {

    // TODO - migrate to mongo

    public Optional<Team> findById(Long id) {
        // This method needs to aggregate the latest team so that the match can be played
        return Optional.empty();
    }
}
