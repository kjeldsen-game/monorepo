package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.player.application.usecases.GeneratePlayersUseCase;
import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.application.usecases.cantera.CanteraInvestmentUsecase;
import com.kjeldsen.player.application.usecases.cantera.EconomyInvestmentUsecase;
import com.kjeldsen.player.application.usecases.economy.PaySalariesTeamUseCase;
import com.kjeldsen.player.application.usecases.economy.UpdateSalariesTeamUseCase;
import com.kjeldsen.player.application.usecases.economy.*;
import com.kjeldsen.player.application.usecases.facilities.UpgradeBuildingUseCase;
import com.kjeldsen.player.application.usecases.fanbase.FansManagementUsecase;
import com.kjeldsen.player.application.usecases.fanbase.UpdateLoyaltyUseCase;
import com.kjeldsen.player.application.usecases.player.PlayerAgingUseCase;
import com.kjeldsen.player.application.usecases.trainings.*;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerPotentialRiseEventMongoRepository;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerTrainingDeclineEventMongoRepository;
import com.kjeldsen.player.rest.api.SimulatorApiDelegate;
import com.kjeldsen.player.rest.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
@Slf4j
public class SimulatorDelegate implements SimulatorApiDelegate {
    private final CanteraInvestmentUsecase canteraInvestmentUsecase;
    private final EconomyInvestmentUsecase economyInvestmentUsecase;
    private final PaySalariesTeamUseCase paySalariesTeamUseCase;
    private final UpdateSalariesTeamUseCase updateSalariesTeamUseCase;
    private final MatchAttendanceIncomeUseCase matchAttendanceIncomeUsecase;
    private final RestaurantIncomeUseCase restaurantIncomeUseCase;
    private final MerchandiseIncomeUseCase merchandiseIncomeUseCase;
    private final FansManagementUsecase fansManagementUsecase;
    private final UpgradeBuildingUseCase upgradeBuildingUseCase;
    private final BuildingMaintenanceExpenseUseCase buildingMaintenanceExpenseUseCase;
    private final UpdateLoyaltyUseCase updateLoyaltyUseCase;
    private final PlayerAgingUseCase playerAgingUseCase;
    private final GeneratePlayersUseCase generatePlayersUseCase;
    private final GetTeamUseCase getTeamUseCase;

    // TMP repositories for simulation
    private final PlayerMongoRepository playerMongoRepository;
    private final PlayerTrainingDeclineEventMongoRepository playerTrainingDeclineEventMongoRepository;
    private final PlayerPotentialRiseEventMongoRepository playerPotentialRiseEventMongoRepository;

    // Training simulations
    private final ProcessPlayerTrainingUseCase processPlayerTrainingUseCase;
    private final ProcessPotentialRiseUseCase processPotentialRiseUseCase;
    private final ProcessDeclineTrainingUseCase processDeclineTrainingUseCase;
    private final ExecuteDeclineTrainingUseCase executeDeclineTrainingUseCase;
    private final ExecutePotentialRiseUseCase executePotentialRiseUseCase;

    // Economy simulations
    private final ResetSponsorIncomeUseCase resetSponsorIncomeUseCase;
    private final ResetBillboardIncomeUseCase resetBillboardIncomeUseCase;


    @Override
    public ResponseEntity<Void> registerInvestmentOnCantera(String teamId, RegisterInvestmentOnCanteraRequest registerInvestmentOnCanteraRequest) {
        Team.TeamId id = Team.TeamId.of(teamId);
        canteraInvestmentUsecase.investToCanteraCategory(id,
            Team.Cantera.Investment.valueOf(registerInvestmentOnCanteraRequest.getInvestment().name()),
            registerInvestmentOnCanteraRequest.getPoints());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> registerEconomicInvestment(String teamId, RegisterEconomicInvestmentRequest registerEconomicInvestmentRequest) {
        economyInvestmentUsecase.invest(Team.TeamId.of(teamId), BigDecimal.valueOf(registerEconomicInvestmentRequest.getAmount()));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateSalaryPayroll(String teamId, SimulateSalaryPayrollRequest simulateSalaryPayrollRequest) {
        IntStream.rangeClosed(1, simulateSalaryPayrollRequest.getWeeks())
            .forEach(index -> paySalariesTeamUseCase.pay(Team.TeamId.of(teamId)));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateMatchIncome(String teamId, SimulateMatchIncomeRequest simulateMatchIncomeRequest) {
        Integer matchAttendance = simulateMatchIncomeRequest.getAwayAttendance() + simulateMatchIncomeRequest.getHomeAttendance();
        matchAttendanceIncomeUsecase.income(Team.TeamId.of(teamId), matchAttendance);
        merchandiseIncomeUseCase.income(Team.TeamId.of(teamId), simulateMatchIncomeRequest.getHomeAttendance());
        restaurantIncomeUseCase.income(Team.TeamId.of(teamId), matchAttendance);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateSalaryIncrease(String teamId) {
        updateSalariesTeamUseCase.update(Team.TeamId.of(teamId));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateFansUpdate(String teamId, SimulateFansUpdateRequest simulateFansUpdateRequest) {
        Team.Fans.ImpactType fansImpactType = Team.Fans.ImpactType.valueOf(simulateFansUpdateRequest.getImpactType().name());
        Team.TeamId id = Team.TeamId.of(teamId);

        fansManagementUsecase.update(id, fansImpactType);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateBuildingUpgrade(String teamId, SimulateBuildingUpgradeRequest simulateBuildingUpgradeRequest) {
        Team.TeamId id = Team.TeamId.of(teamId);
        Team.Buildings.Facility facility = Team.Buildings.Facility.valueOf(simulateBuildingUpgradeRequest.getFacility().name());
        upgradeBuildingUseCase.upgrade(id, facility);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateBuildingMaintenanceExpense(String teamId) {
        buildingMaintenanceExpenseUseCase.expense(Team.TeamId.of(teamId));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateLoyaltyUpdate(String teamId, SimulateLoyaltyUpdateRequest simulateLoyaltyUpdateRequest) {
        Team.TeamId id = Team.TeamId.of(teamId);
        Team.Fans.LoyaltyImpactType loyaltyImpactType = Team.Fans.LoyaltyImpactType.valueOf(
            simulateLoyaltyUpdateRequest.getLoyaltyImpactType().name());
        if (loyaltyImpactType == Team.Fans.LoyaltyImpactType.SEASON_START) {
            updateLoyaltyUseCase.resetLoyalty(id);
        } else if (loyaltyImpactType == Team.Fans.LoyaltyImpactType.SEASON_END) {
            updateLoyaltyUseCase.updateLoyaltySeason(id);
        } else {
            updateLoyaltyUseCase.updateLoyaltyMatch(id, simulateLoyaltyUpdateRequest.getGoals(), loyaltyImpactType);
        }
        return ResponseEntity.ok().build();
    }

    //************************* ECONOMY SIMULATIONS *************************//

    @Override
    public ResponseEntity<Void> simulateResetSponsor(SimulateSponsorResetRequest simulateSponsorResetRequest) {
        resetSponsorIncomeUseCase.reset(Team.Economy.IncomePeriodicity.valueOf(simulateSponsorResetRequest.getPeriodicity().name()));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateBillboardDealReset() {
        resetBillboardIncomeUseCase.reset();
        return ResponseEntity.ok().build();
    }

    //************************* TRAINING SIMULATIONS *************************//


    @Override
    public ResponseEntity<Void> simulatePotentialRisesProcess() {
        processPotentialRiseUseCase.process();
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulatePlayerTrainingsProcess() {
        processPlayerTrainingUseCase.process();
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulatePlayerDeclinesProcess() {
        processDeclineTrainingUseCase.process();
        return ResponseEntity.ok().build();
    }



    @Override
    public ResponseEntity<List<SimulateDaysResponse>> simulateDays(SimulateDaysRequest simulateDaysRequest) {
        List<SimulateDaysResponse> response = new ArrayList<>();
        Team team = getTeamUseCase.get(SecurityUtils.getCurrentUserId());
        Player playerForSimulation = generatePlayersUseCase.generate(1, team.getId()).get(0);
        playerForSimulation.getAge().setYears(18);
        IntStream.rangeClosed(1, simulateDaysRequest.getDays()).forEach(i -> {
            playerAgingUseCase.playerAging(playerForSimulation);

            if (simulateDaysRequest.getExecuteDeclines()) {
//                processDeclineTrainingUseCase.process();
                if (playerForSimulation.getAge().getYears() >= 27 ) {
                    executeDeclineTrainingUseCase.execute(playerForSimulation);
                }
            }
            if (simulateDaysRequest.getExecutePotentialRises()) {
//                processPotentialRiseUseCase.process();
                if (playerForSimulation.getAge().getYears() < 21 ) {
                    executePotentialRiseUseCase.execute(playerForSimulation);
                }
            }
//            if (simulateDaysRequest.getExecuteScheduled()) {
//                processPlayerTrainingUseCase.process();
//            }

        });
        log.info("Player age after the end {}", playerForSimulation.getAge().getYears());

        List<PlayerTrainingDeclineEvent> declineEventList = playerTrainingDeclineEventMongoRepository.findAllByPlayerId(playerForSimulation.getId());
        declineEventList.stream()
            .filter(event -> !event.getPointsBeforeTraining().equals(event.getPointsAfterTraining()))
            .forEach(event -> {
                response.add(new SimulateDaysResponse()
                    .message(event.getFallOfCliffActive() ? "fallOfCliff" : "noFall")
                    .eventType("decline")
                    .playerName(playerForSimulation.getName())
                    .date(event.getOccurredAt().toString())
                    .skill(event.getSkill().name())
                    .pointsBefore(event.getPointsBeforeTraining())
                    .pointsAfter(event.getPointsAfterTraining()));
            });

        List<PlayerPotentialRiseEvent> riseEventList = playerPotentialRiseEventMongoRepository.findAllByPlayerId(playerForSimulation.getId());
        riseEventList.stream()
            .filter(event -> event.getPointsToRise() != 0)
            .forEach(event -> {
                response.add(new SimulateDaysResponse()
                    .message("")
                    .eventType("rise")
                    .playerName(playerForSimulation.getName())
                    .date(event.getOccurredAt().toString())
                    .skill(event.getSkillThatRisen().name())
                    .pointsBefore(event.getPotentialBeforeRaise())
                    .pointsAfter(event.getPotentialAfterRaise()));
            });

        System.out.println(declineEventList.size());
        System.out.println(riseEventList.size());
        System.out.println(playerForSimulation);

        playerPotentialRiseEventMongoRepository.deleteAll(riseEventList);
        playerTrainingDeclineEventMongoRepository.deleteAll(declineEventList);
        playerMongoRepository.delete(playerForSimulation);
        return ResponseEntity.ok(response);
    }
}
