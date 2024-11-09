package com.kjeldsen.player.application.usecases.facilities;

import com.kjeldsen.player.application.usecases.economy.CreateTransactionUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.Transaction;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpgradeBuildingUseCase {

    private final TeamReadRepository teamReadRepository;
    private final TeamWriteRepository teamWriteRepository;
    private final CreateTransactionUseCase createTransactionUseCase;
    private static final BigDecimal FACILITY_PER_LEVEL_UPGRADE = BigDecimal.valueOf(100_000);

    public void upgrade(Team.TeamId teamId, Team.Buildings.Facility facility) {
        log.info("UpgradeBuildingUseCase: upgrading team {} to {}", teamId, facility);

        Team team =  teamReadRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // TODO add BuildingUpgradeEvent

        team.getBuildings().updateFacility(facility);

        // Calculate upgrade cost for the Transaction
        BigDecimal transactionAmount = facility.equals(Team.Buildings.Facility.STADIUM) ?
                BigDecimal.valueOf(100_000) : getUpgradeCost(team, facility);

        // Create a Transaction + realize it
        createTransactionUseCase.create(teamId, transactionAmount, Transaction.TransactionType.BUILDING_UPGRADE);
        teamWriteRepository.save(team);
    }

    private BigDecimal getUpgradeCost(Team team, Team.Buildings.Facility facility) {
        return FACILITY_PER_LEVEL_UPGRADE.multiply(BigDecimal.valueOf(
            team.getBuildings().getFacilities().get(facility).getLevel()));
    }
}
