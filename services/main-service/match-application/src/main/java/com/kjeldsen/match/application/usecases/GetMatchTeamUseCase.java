package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.exceptions.TeamNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetMatchTeamUseCase {

    private final GetMatchUseCase getMatchUseCase;

    public MatchAndTeam getMatchAndTeam(String matchId, String teamId) {
        Match match = getMatchUseCase.get(matchId);

        if (teamId != null) {
            Team team = match.getHome().getId().equals(teamId)
                    ? match.getHome()
                    : match.getAway().getId().equals(teamId)
                            ? match.getAway()
                            : null;
            if (team == null) {
                throw new TeamNotFoundException();
            }

            TeamRole role = match.getHome().getId().equals(teamId)
                    ? TeamRole.HOME
                    : TeamRole.AWAY;

            return new MatchAndTeam(match, team, role);
        }
        return new MatchAndTeam(match, null, null);
    }

    public record MatchAndTeam(Match match, Team team, TeamRole teamRole) {
    }
}
