package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.aggregate.Team;
import com.kjeldsen.match.domain.event.MatchCreatedEvent;
import com.kjeldsen.match.domain.id.EventId;
import com.kjeldsen.match.domain.id.MatchId;
import com.kjeldsen.match.domain.id.TeamId;
import com.kjeldsen.match.domain.provider.InstantProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MatchCreateUseCase {

    public void startMatch(ImmutablePair<Team, Team> teams) {

        final TeamId attackingTeamId = getAttackingTeamId(teams);
        final TeamId defendingTeamId = getDefendingTeamId(teams);

        log.info("Creating match for teams [{}, {}]", attackingTeamId, defendingTeamId);

        // TODO add validation for both teams
        MatchCreatedEvent.builder()
            .eventId(EventId.generate())
            .eventDate(InstantProvider.now())
            .matchId(MatchId.generate())
            .teamIds(List.of(attackingTeamId, defendingTeamId))
            .build();
    }

    private TeamId getAttackingTeamId(ImmutablePair<Team, Team> teams) {
        return teams.getLeft().getId();
    }

    private TeamId getDefendingTeamId(ImmutablePair<Team, Team> teams) {
        return teams.getRight().getId();
    }

}
