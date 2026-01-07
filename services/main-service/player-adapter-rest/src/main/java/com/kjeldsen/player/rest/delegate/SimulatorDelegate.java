package com.kjeldsen.player.rest.delegate;

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
import com.kjeldsen.player.application.usecases.player.rating.ProcessRatingsUseCase;
import com.kjeldsen.player.application.usecases.trainings.decline.ProcessDeclineTrainingUseCase;
import com.kjeldsen.player.application.usecases.trainings.player.ExecutePlayerTrainingUseCase;
import com.kjeldsen.player.application.usecases.trainings.player.ProcessPlayerTrainingUseCase;
import com.kjeldsen.player.application.usecases.trainings.player.SchedulePlayerTrainingUseCase;
import com.kjeldsen.player.application.usecases.trainings.potential.ProcessPotentialRiseUseCase;
import com.kjeldsen.player.application.usecases.trainings.simulator.ExecutePotentialRiseUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;
import com.kjeldsen.player.persistence.mongo.repositories.*;
import com.kjeldsen.player.persistence.mongo.repositories.training.player.PlayerTrainingScheduledEventMongoRepository;
import com.kjeldsen.player.rest.api.SimulatorApiDelegate;
import com.kjeldsen.player.rest.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
@Slf4j
//@PreAuthorize("hasRole('ADMIN')")
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
    private final PlayerTrainingScheduledEventMongoRepository playerTrainingScheduledEventMongoRepository;

    // Training simulations
    private final ProcessPlayerTrainingUseCase processPlayerTrainingUseCase;
    private final ProcessPotentialRiseUseCase processPotentialRiseUseCase;
    private final ProcessDeclineTrainingUseCase processDeclineTrainingUseCase;

//    private final ExecuteDeclineTrainingUseCase executeDeclineTrainingUseCase;
    private final ExecutePotentialRiseUseCase executePotentialRiseUseCase;
    private final ExecutePlayerTrainingUseCase executePlayerTrainingUseCase;
    private final SchedulePlayerTrainingUseCase schedulePlayerTrainingUseCase;

    // Economy simulations
    private final ResetSponsorIncomeUseCase resetSponsorIncomeUseCase;
    private final ResetBillboardIncomeUseCase resetBillboardIncomeUseCase;

    // Rating simulations
    private final ProcessRatingsUseCase processRatingsUseCase;

    @Override
    public ResponseEntity<Void> registerInvestmentOnCantera(String teamId,
            RegisterInvestmentOnCanteraRequest registerInvestmentOnCanteraRequest) {
        Team.TeamId id = Team.TeamId.of(teamId);
        canteraInvestmentUsecase.investToCanteraCategory(id,
                Team.Cantera.Investment.valueOf(registerInvestmentOnCanteraRequest.getInvestment().name()),
                registerInvestmentOnCanteraRequest.getPoints());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<SimulateHelloWorld200Response> simulateHelloWorld() {
        processRatingsUseCase.process();
        return ResponseEntity.ok(new SimulateHelloWorld200Response().message("Hello World"));
    }

    @Override
    public ResponseEntity<Void> registerEconomicInvestment(String teamId,
            RegisterEconomicInvestmentRequest registerEconomicInvestmentRequest) {
        economyInvestmentUsecase.invest(Team.TeamId.of(teamId),
                BigDecimal.valueOf(registerEconomicInvestmentRequest.getAmount()));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateSalaryPayroll(String teamId,
            SimulateSalaryPayrollRequest simulateSalaryPayrollRequest) {
        IntStream.rangeClosed(1, simulateSalaryPayrollRequest.getWeeks())
                .forEach(index -> paySalariesTeamUseCase.pay(Team.TeamId.of(teamId)));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateMatchIncome(String teamId,
            SimulateMatchIncomeRequest simulateMatchIncomeRequest) {
        Integer matchAttendance = simulateMatchIncomeRequest.getAwayAttendance()
                + simulateMatchIncomeRequest.getHomeAttendance();
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
        Team.Fans.ImpactType fansImpactType = Team.Fans.ImpactType
                .valueOf(simulateFansUpdateRequest.getImpactType().name());
        Team.TeamId id = Team.TeamId.of(teamId);

        fansManagementUsecase.update(id, fansImpactType);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateBuildingUpgrade(String teamId,
            SimulateBuildingUpgradeRequest simulateBuildingUpgradeRequest) {
        Team.TeamId id = Team.TeamId.of(teamId);
        Team.Buildings.Facility facility = Team.Buildings.Facility
                .valueOf(simulateBuildingUpgradeRequest.getFacility().name());
        upgradeBuildingUseCase.upgrade(id, facility);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateBuildingMaintenanceExpense(String teamId) {
        buildingMaintenanceExpenseUseCase.expense(Team.TeamId.of(teamId));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateLoyaltyUpdate(String teamId,
            SimulateLoyaltyUpdateRequest simulateLoyaltyUpdateRequest) {
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

    // ************************* ECONOMY SIMULATIONS *************************//

    @Override
    public ResponseEntity<Void> simulateResetSponsor(SimulateSponsorResetRequest simulateSponsorResetRequest) {
        resetSponsorIncomeUseCase
                .reset(Team.Economy.IncomePeriodicity.valueOf(simulateSponsorResetRequest.getPeriodicity().name()));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> simulateBillboardDealReset() {
        resetBillboardIncomeUseCase.reset();
        return ResponseEntity.ok().build();
    }

    // ************************* TRAINING SIMULATIONS *************************//

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
        return ResponseEntity.ok(null);
//        List<SimulateDaysResponse> response = new ArrayList<>();
//        Team team = getTeamUseCase.get(SecurityUtils.getCurrentUserId());
//        Player playerForSimulation;
//        if (simulateDaysRequest.getCreateCustomPlayer()) {
//            Map<PlayerSkill, PlayerSkills> skills = simulateDaysRequest.getPlayer().getSkills().entrySet()
//                    .stream()
//                    .collect(Collectors.toMap(
//                            entry -> PlayerSkill.valueOf(entry.getKey()),
//                            entry -> PlayerSkills.builder()
//                                    .actual(entry.getValue().getActual())
//                                    .potential(entry.getValue().getPotential())
//                                    .build()));
//
//            playerForSimulation = Player.builder()
//                    .id(Player.PlayerId.generate())
//                    .name(Faker.instance().name().name())
//                    .teamId(getTeamUseCase.get(SecurityUtils.getCurrentUserId()).getId())
//                    .age(PlayerAge.builder()
//                            .years(simulateDaysRequest.getPlayer().getAge().getYears())
//                            .months(simulateDaysRequest.getPlayer().getAge().getMonths().doubleValue())
//                            .days(simulateDaysRequest.getPlayer().getAge().getDays().doubleValue())
//                            .build())
//                    .actualSkills(skills)
//                    .bloomYear(simulateDaysRequest.getPlayer().getBloomYear())
//                    .build();
//        } else {
//            playerForSimulation = generatePlayersUseCase.generate(1, team.getId()).get(0);
//        }
//        AtomicInteger nextSkillToTrain = new AtomicInteger(2);
//        playerMongoRepository.save(playerForSimulation);
//        schedulePlayerTrainingUseCase.schedule(playerForSimulation.getId(),
//                PlayerSkill.valueOf(simulateDaysRequest.getSkills().get(0).name()));
//        schedulePlayerTrainingUseCase.schedule(playerForSimulation.getId(),
//                PlayerSkill.valueOf(simulateDaysRequest.getSkills().get(1).name()));
//
//        IntStream.rangeClosed(1, simulateDaysRequest.getDays()).forEach(i -> {
//            playerAgingUseCase.playerAging(playerForSimulation);
//
//            if (simulateDaysRequest.getExecuteDeclines()) {
//                if (playerForSimulation.getAge().getYears() >= 27) {
////                    executeDeclineTrainingUseCase.execute(playerForSimulation);
//                }
//            }
//            if (simulateDaysRequest.getExecutePotentialRises()) {
//                if (playerForSimulation.getAge().getYears() < 21) {
//                    executePotentialRiseUseCase.execute(playerForSimulation);
//                }
//            }
//            if (simulateDaysRequest.getExecuteScheduled()) {
//                List<PlayerTrainingScheduledEvent> playerTrainingScheduledEvents = playerTrainingScheduledEventMongoRepository
//                        .findAllWhereStatusIsActive();
//                playerTrainingScheduledEvents = playerTrainingScheduledEvents.stream()
//                        .filter(e -> e.getPlayerId().equals(playerForSimulation.getId()))
//                        .toList();
//                playerTrainingScheduledEvents.forEach(scheduledTraining -> {
//                    Optional<PlayerTrainingEvent> latestPlayerTrainingEvent = playerTrainingEventReadRepository
//                            .findLastByPlayerTrainingEvent(scheduledTraining.getId().value());
//
//                    if (latestPlayerTrainingEvent.isPresent()) {
//                        PlayerTrainingEvent trainingEvent = latestPlayerTrainingEvent.get();
//
//                        if (trainingEvent.getActualPoints() == 100) {
//                            log.info("Training reached the maximum value for actual, choosing another skill {} {}",
//                                    scheduledTraining.getSkill(),
//                                    simulateDaysRequest.getSkills().get(nextSkillToTrain.get()));
//
//                            scheduledTraining.setStatus(PlayerTrainingScheduledEvent.Status.INACTIVE);
//                            playerTrainingScheduledEventMongoRepository.save(scheduledTraining);
//                            schedulePlayerTrainingUseCase.schedule(playerForSimulation.getId(), PlayerSkill
//                                    .valueOf(simulateDaysRequest.getSkills().get(nextSkillToTrain.get()).name()));
//                            nextSkillToTrain.incrementAndGet();
//                            return;
//                        }
//
//                        if (trainingEvent.getPointsAfterTraining() > trainingEvent.getPointsBeforeTraining()) {
//                            // Points increased, the new training is starting from 1 day
//                            log.info("There was already successful training for player {} skill {}, set day to 1!",
//                                    trainingEvent.getPlayerId(), trainingEvent.getSkill());
//                            executePlayerTrainingUseCase.execute(scheduledTraining.getPlayerId(),
//                                    scheduledTraining.getSkill(),
//                                    1, scheduledTraining.getId().value());
//                        } else {
//                            log.info("The previous training was not successful, setting day to {}",
//                                    trainingEvent.getCurrentDay() + 1);
//                            executePlayerTrainingUseCase.execute(scheduledTraining.getPlayerId(),
//                                    scheduledTraining.getSkill(),
//                                    trainingEvent.getCurrentDay() + 1, scheduledTraining.getId().value());
//                        }
//                    } else {
//                        log.info("Training is not present setting the current day directly to 1 {}",
//                                scheduledTraining.getSkill());
//                        executePlayerTrainingUseCase.execute(scheduledTraining.getPlayerId(),
//                                scheduledTraining.getSkill(), 1,
//                                scheduledTraining.getId().value());
//                    }
//                });
//
//            }
//
//        });
//        log.info("Player age after the end {}", playerForSimulation.getAge().getYears());
//
//        List<PlayerTrainingScheduledEvent> playerTrainingScheduledEvents = playerTrainingScheduledEventMongoRepository
//                .findAllByPlayerId(playerForSimulation.getId());
//
//        List<PlayerTrainingEvent> playerTrainingEvents = playerTrainingEventMongoRepository
//                .findAllByPlayerId(playerForSimulation.getId());
//        playerTrainingEvents.stream()
//                .filter(event -> !event.getPointsAfterTraining().equals(event.getPointsBeforeTraining()))
//                .forEach(event -> {
//                    response.add(new SimulateDaysResponse()
//                            .message(event.isBloom() ? "bloom active" : "bloom inactive")
//                            .eventType("training")
//                            .playerName(playerForSimulation.getName())
//                            .date(event.getOccurredAt().toString())
//                            .skill(event.getSkill().name())
//                            .points(event.getPoints())
//                            .pointsBefore(event.getPointsBeforeTraining())
//                            .pointsAfter(event.getPointsAfterTraining())
//                            .potentialPoints(event.getPotentialPoints())
//                            .actualPoints(event.getActualPoints()));
//
//                });
//
//        List<PlayerTrainingDeclineEvent> declineEventList = playerTrainingDeclineEventMongoRepository
//                .findAllByPlayerId(playerForSimulation.getId());
//        declineEventList.stream()
//                .filter(event -> !event.getPointsBeforeTraining().equals(event.getPointsAfterTraining()))
//                .forEach(event ->
//                    response.add(new SimulateDaysResponse()
//                            .message(event.getFallOfCliffActive() ? "fallOfCliff" : "noFall")
//                            .eventType("decline")
//                            .playerName(playerForSimulation.getName())
//                            .date(event.getOccurredAt().toString())
//                            .skill(event.getSkill().name())
//                            .pointsBefore(event.getPointsBeforeTraining())
//                            .pointsAfter(event.getPointsAfterTraining())));
//
//        List<PlayerPotentialRiseEvent> riseEventList = playerPotentialRiseEventMongoRepository
//                .findAllByPlayerId(playerForSimulation.getId());
//        riseEventList.stream()
//                .filter(event -> event.getPointsToRise() != 0)
//                .forEach(event ->
//                    response.add(new SimulateDaysResponse()
//                            .message("")
//                            .eventType("rise")
//                            .playerName(playerForSimulation.getName())
//                            .date(event.getOccurredAt().toString())
//                            .skill(event.getSkillThatRisen().name())
//                            .pointsBefore(event.getPotentialBeforeRaise())
//                            .pointsAfter(event.getPotentialAfterRaise())));
//
//        playerTrainingEventMongoRepository.deleteAll(playerTrainingEvents);
//        playerTrainingScheduledEventMongoRepository.deleteAll(playerTrainingScheduledEvents);
//        playerPotentialRiseEventMongoRepository.deleteAll(riseEventList);
//        playerTrainingDeclineEventMongoRepository.deleteAll(declineEventList);
//        playerMongoRepository.delete(playerForSimulation);
//        return ResponseEntity.ok(response);
    }
}
