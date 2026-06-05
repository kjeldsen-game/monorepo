package com.kjeldsen.match.repositories;

import com.kjeldsen.match.models.Team;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class TeamRepository {

    // TODO - migrate to mongo; in-memory store used for local demo
    private final Map<Long, Team> store = new ConcurrentHashMap<>();

    public Optional<Team> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Team save(Team team) {
        store.put(team.getId(), team);
        return team;
    }
}
