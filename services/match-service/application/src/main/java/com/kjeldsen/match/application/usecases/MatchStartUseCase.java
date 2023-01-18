package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.aggregate.Team;
import com.kjeldsen.match.domain.events.MatchStartedEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MatchStartUseCase {

    public void startMatch(ImmutablePair<Team, Team> teams) {

        // TODO log which teams are playing
        // TODO add validation for both teams
        MatchStartedEvent.builder()
            .eventId("adsd")
            .date(Instant.now())
            .matchId("UUID")
            .teamIds(List.of(getAttackingTeamId(teams), getDefendingTeamId(teams)))
            .build();
    }

    private String getAttackingTeamId(ImmutablePair<Team, Team> teams) {
        return teams.getLeft().getId();
    }

    private String getDefendingTeamId(ImmutablePair<Team, Team> teams) {
        return teams.getRight().getId();
    }

}
