package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class BuildingMaintenanceExpenseUseCase {

    private final TeamReadRepository teamReadRepository;
    private final CreateTransactionUseCase createTransactionUseCase;

    public void expense(Team.TeamId teamId) {
        log.info("BuildingMaintenanceExpenseUseCase for team {}", teamId);

        Team team = teamReadRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        // TODO To be discussed if there should be Transaction per Facility or it's okay that all happened in one
        BigDecimal maintenanceCost = team.getBuildings().getFacilities().values().stream().map(
            Team.Buildings.FacilityData::getMaintenanceCost).reduce(BigDecimal.ZERO, BigDecimal::add)
            .add(team.getBuildings().getStadium().getMaintenanceCost());

        createTransactionUseCase.create(teamId, maintenanceCost, Transaction.TransactionType.BUILDING_MAINTENANCE);
    }
}
