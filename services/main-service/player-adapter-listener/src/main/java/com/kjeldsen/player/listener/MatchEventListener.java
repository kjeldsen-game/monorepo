package com.kjeldsen.player.listener;

import com.kjeldsen.player.application.usecases.economy.MatchAttendanceIncomeUsecase;
import com.kjeldsen.player.application.usecases.economy.MerchandiseIncomeUseCase;
import com.kjeldsen.player.application.usecases.economy.RestaurantIncomeUseCase;
import com.kjeldsen.player.application.usecases.fanbase.FansManagementUsecase;
import com.kjeldsen.player.application.usecases.fanbase.UpdateLoyaltyUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.MatchEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MatchEventListener {

    private final MatchAttendanceIncomeUsecase matchAttendanceIncomeUsecase;
    private final FansManagementUsecase fansManagementUsecase;
    private final MerchandiseIncomeUseCase merchandiseIncomeUseCase;
    private final RestaurantIncomeUseCase restaurantIncomeUseCase;
    private final UpdateLoyaltyUseCase updateLoyaltyUseCase;

    @EventListener
    public void handleMatchEvent(MatchEvent matchEvent) {
      log.info("MatchEvent received: {}", matchEvent);

      Integer totalMatchAttendance = matchEvent.getAwayAttendance() + matchEvent.getHomeAttendance();
      Team.TeamId homeTeamId = Team.TeamId.of(matchEvent.getHomeTeamId());
      Team.TeamId awayTeamId = Team.TeamId.of(matchEvent.getAwayTeamId());

      // Match Income UseCases
      matchAttendanceIncomeUsecase.income(homeTeamId, totalMatchAttendance);
      merchandiseIncomeUseCase.income(homeTeamId, matchEvent.getHomeAttendance());
      restaurantIncomeUseCase.income(homeTeamId, totalMatchAttendance);

      // SponsorIncome per match win income
      Team.TeamId winningTeamId = getWinningTeam(matchEvent);
      if (winningTeamId != null) {
          // TODO missing per win income Sponsor
      }


      // Fans UseCases
      updateLoyaltyUseCase.updateLoyaltyMatch(homeTeamId, matchEvent.getHomeScore(),
          Team.Fans.LoyaltyImpactType.valueOf(getFansImpact(matchEvent.getHomeScore(), matchEvent.getAwayScore(), true).name()));
      updateLoyaltyUseCase.updateLoyaltyMatch(awayTeamId, matchEvent.getAwayScore(),
          Team.Fans.LoyaltyImpactType.valueOf(getFansImpact(matchEvent.getHomeScore(), matchEvent.getAwayScore(), false).name()));

      fansManagementUsecase.update(homeTeamId,
        getFansImpact(matchEvent.getHomeScore(), matchEvent.getAwayScore(), true));
      fansManagementUsecase.update(awayTeamId,
        getFansImpact(matchEvent.getHomeScore(), matchEvent.getAwayScore(), false));
    }

    private Team.TeamId getWinningTeam(MatchEvent matchEvent) {
        if (matchEvent.getHomeScore() > matchEvent.getAwayScore()) {
            return Team.TeamId.of(matchEvent.getHomeTeamId());
        } else if (matchEvent.getAwayScore() > matchEvent.getHomeScore()) {
            return Team.TeamId.of(matchEvent.getAwayTeamId());
        } else {
            return null;
        }
    }

    private Team.Fans.ImpactType getFansImpact(Integer homeScore, Integer awayScore, boolean isHomeTeam) {
        if (homeScore > awayScore) {
            return isHomeTeam ? Team.Fans.ImpactType.MATCH_WIN : Team.Fans.ImpactType.MATCH_LOSS;
        } else if (homeScore < awayScore) {
            return isHomeTeam ? Team.Fans.ImpactType.MATCH_LOSS : Team.Fans.ImpactType.MATCH_WIN;
        } else {
            return Team.Fans.ImpactType.MATCH_DRAW;
        }
    }
}
