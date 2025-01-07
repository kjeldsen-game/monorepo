package com.kjeldsen.match.application.usecases;


import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.repositories.MatchReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetMatchTeamUseCase {

    private final MatchReadRepository matchReadRepository;

    public Team get(String teamId, String matchId) {
        Match match = matchReadRepository.findOneById(matchId).orElseThrow(
            () -> new RuntimeException("Match not found"));

        // Retrieve the desired Team
        Team team = match.getHome().getId().equals(teamId)
            ? match.getHome()
            : match.getAway().getId().equals(teamId)
            ? match.getAway()
            : null;
        if (team == null) {
            throw new RuntimeException("Team not found");
        }
        return team;
    }
}
