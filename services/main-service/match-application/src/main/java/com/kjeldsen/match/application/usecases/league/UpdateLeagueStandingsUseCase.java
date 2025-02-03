package com.kjeldsen.match.application.usecases.league;


import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.repositories.LeagueReadRepository;
import com.kjeldsen.match.domain.repositories.LeagueWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateLeagueStandingsUseCase {

    private final LeagueWriteRepository leagueWriteRepository;
    private final GetLeagueUseCase getLeagueUseCase;

    public synchronized void update(String leagueId, String homeTeamId, String awayTeamId, Integer homeScore, Integer awayScore) {
        log.info("UpdateLeagueStandingsUseCase for league={} homeId={} awayId={} homeScore={} awayScore={}", leagueId,
            homeTeamId, awayTeamId, homeScore, awayScore);

        League league = getLeagueUseCase.get(leagueId);

        Map<String, String> matchWinner = getWinnerAndLoser(homeScore, awayScore, homeTeamId, awayTeamId);
        league.getTeams().get(homeTeamId).updateGoals(homeScore, awayScore);
        league.getTeams().get(awayTeamId).updateGoals(awayScore,homeScore);

        if (matchWinner == null) {
            league.getTeams().get(homeTeamId).handleDraw();
            league.getTeams().get(awayTeamId).handleDraw();
        } else {
            league.getTeams().get(matchWinner.get("winner")).handleWin();
            league.getTeams().get(matchWinner.get("loser")).handleLoss();
        }
        updatePositions(league.getTeams());
        leagueWriteRepository.save(league);
    }

    private void updatePositions(Map<String, League.LeagueStats> standings) {
        List<Map.Entry<String, League.LeagueStats>> sortedStandings = standings.entrySet()
            .stream()
            .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().getPoints(), entry1.getValue().getPoints()))
            .toList();

        int position = 1;
        for (Map.Entry<String, League.LeagueStats> entry : sortedStandings) {
            entry.getValue().setPosition(position);
            position++;
        }
    }

    private Map<String, String> getWinnerAndLoser(Integer homeScore, Integer awayScore, String homeTeamId, String awayTeamId) {
        Map<String, String> result = new HashMap<>();
        if (homeScore > awayScore) {
            result.put("winner", homeTeamId);
            result.put("loser", awayTeamId);
        } else if (awayScore > homeScore) {
            result.put("winner", awayTeamId);
            result.put("loser", homeTeamId);
        } else {
            return null;
        }
        return result;
    }
}
