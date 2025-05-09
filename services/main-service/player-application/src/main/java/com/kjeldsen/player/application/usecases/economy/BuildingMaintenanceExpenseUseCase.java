package com.kjeldsen.player.application.usecases.economy;

import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class BuildingMaintenanceExpenseUseCase {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetTeamUseCase getTeamUseCase;

    public void expense(Team.TeamId teamId) {
        log.info("BuildingMaintenanceExpenseUseCase for team {}", teamId);

        Team team = getTeamUseCase.get(teamId);

        // TODO To be discussed if there should be Transaction per Facility or it's okay that all happened in one
        BigDecimal maintenanceCost = team.getBuildings().getFacilities().values().stream().map(
            Team.Buildings.FacilityData::getMaintenanceCost).reduce(BigDecimal.ZERO, BigDecimal::add)
            .add(team.getBuildings().getStadium().getMaintenanceCost());

        createTransactionUseCase.create(teamId, maintenanceCost.negate(), Transaction.TransactionType.BUILDING_MAINTENANCE);
    }
}
