package com.kjeldsen.match.application.usecases;


import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.entities.TeamRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetMatchTeamUseCase {

    private final GetMatchUseCase getMatchUseCase;

    public MatchAndTeam getMatchAndTeam(String matchId, String teamId) {
        log.info("GetMatchTeamUseCase to get MatchAndTeam for teamId={} matchId={}", teamId, matchId);
        Match match = getMatchUseCase.get(matchId);

        Team team = match.getHome().getId().equals(teamId)
            ? match.getHome()
            : match.getAway().getId().equals(teamId)
            ? match.getAway()
            : null;
        if (team == null) {
            throw new RuntimeException("Team not found");
        }

        TeamRole role = match.getHome().getId().equals(teamId)
            ? TeamRole.HOME : TeamRole.AWAY;

        return new MatchAndTeam(match, team, role);
    }

        public record MatchAndTeam(Match match, Team team, TeamRole teamRole) {
    }
}
