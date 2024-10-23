package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.*;
import com.kjeldsen.player.application.usecases.economy.*;
import com.kjeldsen.player.application.usecases.facilities.UpgradeBuildingUseCase;
import com.kjeldsen.player.application.usecases.fanbase.FansManagementUsecase;
import com.kjeldsen.player.application.usecases.fanbase.UpdateLoyaltyUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.rest.api.SimulatorApiDelegate;
import com.kjeldsen.player.rest.mapper.PlayerDeclineResponseMapper;
import com.kjeldsen.player.rest.mapper.PlayerMapper;
import com.kjeldsen.player.rest.mapper.PlayerPotentialRiseResponseMapper;
import com.kjeldsen.player.rest.mapper.PlayerTrainingResponseMapper;
import com.kjeldsen.player.rest.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class SimulatorDelegate implements SimulatorApiDelegate {
    private static final Integer MAX_AGE = 21;
    private final ScheduleTrainingUseCase scheduleTrainingUseCase;
    private final SchedulePotentialRiseUseCase schedulePotentialRiseUseCase;
    private final FindAndProcessScheduledTrainingUseCase findAndProcessScheduledTrainingUseCase;
    private final FindAndProcessScheduledPotentialUseCase findAndProcessScheduledPotentialRiseUseCase;
    private final GenerateSingleDeclineTrainingUseCase generateSingleDeclineTrainingUseCase;
    private final CanteraInvestmentUsecase canteraInvestmentUsecase;
    private final EconomyInvestmentUsecase economyInvestmentUsecase;
    private final SponsorIncomeUsecase sponsorIncomeUsecase;
    private final PaySalariesTeamUseCase paySalariesTeamUseCase;
    private final UpdateSalariesTeamUseCase updateSalariesTeamUseCase;
    private final MatchAttendanceIncomeUsecase matchAttendanceIncomeUsecase;
    private final RestaurantIncomeUseCase restaurantIncomeUseCase;
    private final MerchandiseIncomeUseCase merchandiseIncomeUseCase;
    private final FansManagementUsecase fansManagementUsecase;
    private final UpdateTeamPricingUsecase updateTeamPricingUsecase;
    private final UpgradeBuildingUseCase upgradeBuildingUseCase;
    private final BuildingMaintenanceExpenseUseCase buildingMaintenanceExpenseUseCase;
    private final UpdateLoyaltyUseCase updateLoyaltyUseCase;
    private final SignBillboardIncomeUseCase signBillboardIncomeUseCase;

    @Override
    public ResponseEntity<PlayerHistoricalPotentialRiseResponse> registerSimulatedScheduledPotentialRise(
                String playerId,
                RegisterSimulatedScheduledPotentialRiseRequest registerSimulatedScheduledPotentialRiseRequest) {

        schedulePotentialRiseUseCase.generate(
                Player.PlayerId.of(playerId),
                registerSimulatedScheduledPotentialRiseRequest.getDaysToSimulate()
        );

     List<PlayerPotentialRiseEvent> potentialRises = findAndProcessScheduledPotentialRiseUseCase.findAndProcess(InstantProvider.nowAsLocalDate())
        .stream()
        .filter(playerPotentialRiseEvent -> playerPotentialRiseEvent.getPlayerId().equals(Player.PlayerId.of(playerId)))
        .toList();

        return ResponseEntity.ok(new PlayerHistoricalPotentialRiseResponse()
        .playerId(playerId)
            .potentialRises(potentialRises.stream()
            .map(PlayerPotentialRiseResponseMapper.INSTANCE::fromPlayerPotentialRiseEvent)
            .toList()
            ));
    }
    @Override
    public ResponseEntity<PlayerHistoricalTrainingResponse> registerSimulatedScheduledTraining(
            String playerId,
            RegisterSimulatedScheduledTrainingRequest registerSimulatedScheduledTrainingRequest) {

        registerSimulatedScheduledTrainingRequest.getSkills()
                .forEach(skillsToTrain -> scheduleTrainingUseCase.generate(
                        Player.PlayerId.of(playerId),
                        PlayerMapper.INSTANCE.map(skillsToTrain.getValue()),
                        registerSimulatedScheduledTrainingRequest.getDays()
                ));

//        List<PlayerTrainingEvent> trainings = findAndProcessScheduledTrainingUseCase.findAndProcess(InstantProvider.nowAsLocalDate())
//                .stream()
//                .filter(playerTrainingEvent -> playerTrainingEvent.getPlayerId().equals(Player.PlayerId.of(playerId)))
//                .toList();

        return ResponseEntity.ok(new PlayerHistoricalTrainingResponse()
                .playerId(playerId)
                .trainings(Collections.EMPTY_LIST)
//                .trainings(trainings.stream()
//                        .map(PlayerTrainingResponseMapper.INSTANCE::fromPlayerTrainingEvent)
//                        .toList()
                );
    }

    @Override
    public ResponseEntity<List<PlayerDeclineResponse>> registerSimulatedDecline(String playerId,
        RegisterSimulatedDeclineRequest registerSimulatedDeclineRequest) {

        List<PlayerDeclineResponse> declineEvents = new ArrayList<>();

        final AtomicInteger currentDayForDecline = new AtomicInteger(1);
        IntStream.rangeClosed(1, registerSimulatedDeclineRequest.getDaysToDecline())
            .forEach(i -> {
                PlayerTrainingDeclineEvent declineEvent = generateSingleDeclineTrainingUseCase.generate(
                    Player.PlayerId.of(playerId),
                    currentDayForDecline.getAndIncrement(),
                    registerSimulatedDeclineRequest.getDeclineSpeed());

                declineEvents.add(PlayerDeclineResponseMapper.INSTANCE.map(declineEvent));

                if (declineEvent.getPointsToSubtract() > 0) {
                    currentDayForDecline.set(1);
                }
            });

        return ResponseEntity.ok(declineEvents);
    }

    @Override
    public ResponseEntity<Void> registerInvestmentOnCantera(String teamId, RegisterInvestmentOnCanteraRequest registerInvestmentOnCanteraRequest) {
        Team.TeamId id = Team.TeamId.of(teamId);
        canteraInvestmentUsecase.investToCanteraCategory(id, Team.Cantera.Investment.valueOf(registerInvestmentOnCanteraRequest.getInvestment().name()),
                registerInvestmentOnCanteraRequest.getPoints());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> registerEconomicInvestment(String teamId, RegisterEconomicInvestmentRequest registerEconomicInvestmentRequest) {
        economyInvestmentUsecase.invest(Team.TeamId.of(teamId), BigDecimal.valueOf(registerEconomicInvestmentRequest.getAmount()));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> registerSponsorIncome(String teamId, RegisterSponsorIncomeRequest registerSponsorIncomeRequest) {

        Integer weeks = registerSponsorIncomeRequest.getWeeks();
        Integer wins = registerSponsorIncomeRequest.getWins();

        IntStream.rangeClosed(1, weeks).forEach(index -> registerSponsorIncomeRequest.getSponsors()
            .stream()
            .filter(sponsor -> SponsorPeriodicity.WEEKLY.equals(sponsor.getPeriodicity()))
            .forEach(sponsor -> {
                Team.Economy.IncomeMode mode = Team.Economy.IncomeMode.valueOf(sponsor.getMode().name());
                sponsorIncomeUsecase.incomeWeekly(Team.TeamId.of(teamId), mode, wins);
            }));

        int years = weeks / 13; // 13 weeks in a season
        IntStream.rangeClosed(1, years).forEach(index -> registerSponsorIncomeRequest.getSponsors()
            .stream()
            .filter(sponsor -> SponsorPeriodicity.ANNUAL.equals(sponsor.getPeriodicity()))
            .forEach(sponsor -> {
                Team.Economy.IncomeMode mode = Team.Economy.IncomeMode.valueOf(sponsor.getMode().name());
                sponsorIncomeUsecase.incomeAnnual(Team.TeamId.of(teamId), mode, wins);
            }));

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
    public ResponseEntity<Void> simulatePricingUpdate(String teamId, SimulatePricingUpdateRequest simulatePricingUpdateRequest) {
        Team.TeamId id = Team.TeamId.of(teamId);
        Team.Economy.PricingType pricingType = Team.Economy.PricingType.valueOf(
                simulatePricingUpdateRequest.getPricingType().name());
        updateTeamPricingUsecase.update(id, simulatePricingUpdateRequest.getPrice(), pricingType);
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
    public ResponseEntity<Void> simulateBillboardSelection(String teamId, SimulateBillboardSelectionRequest simulateBillboardSelectionRequest) {
        Team.Economy.BillboardIncomeType mode = Team.Economy.BillboardIncomeType.valueOf(
            simulateBillboardSelectionRequest.getMode().name());
        signBillboardIncomeUseCase.sign(Team.TeamId.of(teamId), mode);
        return  ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateLoyaltyUpdate(String teamId, SimulateLoyaltyUpdateRequest simulateLoyaltyUpdateRequest) {
        Team.TeamId id = Team.TeamId.of(teamId);
        Team.Fans.LoyaltyImpactType loyaltyImpactType = Team.Fans.LoyaltyImpactType.valueOf(simulateLoyaltyUpdateRequest.getLoyaltyImpactType().name());
        if (loyaltyImpactType == Team.Fans.LoyaltyImpactType.SEASON_START)
            updateLoyaltyUseCase.resetLoyalty(id);
        else if (loyaltyImpactType == Team.Fans.LoyaltyImpactType.SEASON_END)
            updateLoyaltyUseCase.updateLoyaltySeason(id);
        else
            updateLoyaltyUseCase.updateLoyaltyMatch(id, simulateLoyaltyUpdateRequest.getGoals(), loyaltyImpactType);
        return ResponseEntity.ok().build();
    }
}
