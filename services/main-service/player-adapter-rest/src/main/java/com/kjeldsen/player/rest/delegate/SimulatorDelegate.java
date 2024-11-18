package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.cantera.CanteraInvestmentUsecase;
import com.kjeldsen.player.application.usecases.cantera.EconomyInvestmentUsecase;
import com.kjeldsen.player.application.usecases.economy.PaySalariesTeamUseCase;
import com.kjeldsen.player.application.usecases.economy.UpdateSalariesTeamUseCase;
import com.kjeldsen.player.application.usecases.economy.*;
import com.kjeldsen.player.application.usecases.facilities.UpgradeBuildingUseCase;
import com.kjeldsen.player.application.usecases.fanbase.FansManagementUsecase;
import com.kjeldsen.player.application.usecases.fanbase.UpdateLoyaltyUseCase;
import com.kjeldsen.player.application.usecases.trainings.ProcessDeclineTrainingUseCase;
import com.kjeldsen.player.application.usecases.trainings.ProcessPlayerTrainingUseCase;
import com.kjeldsen.player.application.usecases.trainings.ProcessPotentialRiseUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.rest.api.SimulatorApiDelegate;
import com.kjeldsen.player.rest.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

    // Training simulations
    private final ProcessPlayerTrainingUseCase processPlayerTrainingUseCase;
    private final ProcessPotentialRiseUseCase processPotentialRiseUseCase;
    private final ProcessDeclineTrainingUseCase processDeclineTrainingUseCase;

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

//    @Override
//    public ResponseEntity<Void> registerSponsorIncome(String teamId, RegisterSponsorIncomeRequest registerSponsorIncomeRequest) {
//
//        Integer weeks = registerSponsorIncomeRequest.getWeeks();
//        Integer wins = registerSponsorIncomeRequest.getWins();
//
//        IntStream.rangeClosed(1, weeks).forEach(index -> registerSponsorIncomeRequest.getSponsors()
//            .stream()
//            .filter(sponsor -> SponsorPeriodicity.WEEKLY.equals(sponsor.getPeriodicity()))
//            .forEach(sponsor -> {
//                Team.Economy.IncomeMode mode = Team.Economy.IncomeMode.valueOf(sponsor.getMode().name());
//                sponsorIncomeUsecase.incomeWeekly(Team.TeamId.of(teamId), mode, wins);
//            }));
//
//        int years = weeks / 13; // 13 weeks in a season
//        IntStream.rangeClosed(1, years).forEach(index -> registerSponsorIncomeRequest.getSponsors()
//            .stream()
//            .filter(sponsor -> SponsorPeriodicity.ANNUAL.equals(sponsor.getPeriodicity()))
//            .forEach(sponsor -> {
//                Team.Economy.IncomeMode mode = Team.Economy.IncomeMode.valueOf(sponsor.getMode().name());
//                sponsorIncomeUsecase.incomeAnnual(Team.TeamId.of(teamId), mode, wins);
//            }));
//
//        return ResponseEntity.ok().build();
//    }

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
        return ResponseEntity.ok().build();    }

    @Override
    public ResponseEntity<Void> simulatePlayerDeclinesProcess() {
        processDeclineTrainingUseCase.process();
        return ResponseEntity.ok().build();    }
}
