package com.kjeldsen.player.application.usecases.fanbase;

import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class FansManagementUsecase {

    private static final Integer LAST_SEASON_POSITION = 12;
    private static final Integer FANS_PER_RANK = 100;
    private final GetTeamUseCase getTeamUseCase;
    private final TeamWriteRepository teamWriteRepository;


    public void update(Team.TeamId teamId, Team.Fans.ImpactType impactType) {
        log.info("FansManagementUsecase team {} impact type {}", teamId, impactType);

        Team team =  getTeamUseCase.get(teamId);

        Integer newFans = impactType.equals(Team.Fans.ImpactType.SEASON_END) ?
                getSeasonEndFans(team.getLeagueStats()) : impactType.getFansImpact();


        team.getFans().updateFans(newFans);
//        team.getFans().updateTotalFans(fansEvent);
        teamWriteRepository.save(team);
    }


    private Integer getSeasonEndFans(Map<Integer, Team.LeagueStats> teamLeagueStats) {
        // Get the Team's position in current season and get the fans
        Integer seasonNumber = Collections.max(teamLeagueStats.keySet());
        Integer seasonPosition = teamLeagueStats.get(seasonNumber).getTablePosition();
        Integer seasonEndFans = getFans(teamLeagueStats.get(seasonNumber).getTablePosition());

        // Compare the Team's position in the current season vs previous season and get the fans
        Integer prevSeasonPosition;
        try {
            prevSeasonPosition = teamLeagueStats.get(seasonNumber - 1).getTablePosition();
        } catch (Exception e) {
            log.info("No record for previous season, setting prev season to last place");
            prevSeasonPosition = LAST_SEASON_POSITION; // If team was not previous season in Kjeldsen set position to last
        }

        Integer compareSeasonsFans = compareSeasons(seasonPosition, prevSeasonPosition);
        return seasonEndFans + compareSeasonsFans;
    }

    private Integer compareSeasons(Integer seasonPosition, Integer prevSeasonPosition) {
        int positionDiff = prevSeasonPosition - seasonPosition;
        return positionDiff > 0 ? Math.abs(positionDiff * FANS_PER_RANK) : - Math.abs(positionDiff * FANS_PER_RANK);
    }

    private Integer getFans(Integer leaguePosition) {
        return switch (leaguePosition) {
            case 1 -> 500;
            case 2 -> 400;
            case 3 -> 300;
            case 4 -> 200;
            case 5 -> 150;
            case 6 -> 100;
            default ->  -500;
        };
    }
}
