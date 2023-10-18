package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.player.application.usecases.cantera.CanteraBuildingsInvestmentUseCase;
import com.kjeldsen.player.application.usecases.cantera.CanteraEconomyInvestmentUseCase;
import com.kjeldsen.player.application.usecases.cantera.CanteraTraditionInvestmentUseCase;
import com.kjeldsen.player.application.usecases.economy.*;
import com.kjeldsen.player.application.usecases.training.FindAndProcessScheduledTrainingUseCase;
import com.kjeldsen.player.application.usecases.training.GenerateSingleDeclineTrainingUseCase;
import com.kjeldsen.player.application.usecases.training.GenerateTrainingUseCase;
import com.kjeldsen.player.application.usecases.training.ScheduleTrainingUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.rest.api.SimulatorApiDelegate;
import com.kjeldsen.player.rest.mapper.PlayerDeclineResponseMapper;
import com.kjeldsen.player.rest.mapper.PlayerMapper;
import com.kjeldsen.player.rest.mapper.PlayerTrainingResponseMapper;
import com.kjeldsen.player.rest.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class SimulatorDelegate implements SimulatorApiDelegate {

    private final ScheduleTrainingUseCase scheduleTrainingUseCase;
    private final FindAndProcessScheduledTrainingUseCase findAndProcessScheduledTrainingUseCase;
    private final GenerateSingleDeclineTrainingUseCase generateSingleDeclineTrainingUseCase;
    private final CanteraEconomyInvestmentUseCase canteraEconomyInvestmentUsecase;
    private final CanteraTraditionInvestmentUseCase canteraTraditionInvestmentUsecase;
    private final CanteraBuildingsInvestmentUseCase canteraBuildingsInvestmentUsecase;
    private final EconomyInvestmentUseCase economyInvestmentUsecase;
    private final AnnualIncomeSponsorUseCase annualIncomeSponsorUsecase;
    private final WeeklyIncomeSponsorUseCase weeklyIncomeSponsorUsecase;

    private final MatchIncomeAttendanceUseCase matchIncomeAttendanceUseCase;
    private final PaySalariesTeamUseCase paySalariesTeamUseCase;
    private final UpdateSalariesTeamUseCase updateSalariesTeamUseCase;
    private final GenerateTrainingUseCase generateTrainingUseCase;

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

        List<PlayerTrainingEvent> trainings = findAndProcessScheduledTrainingUseCase.findAndProcess(InstantProvider.nowAsLocalDate())
            .stream()
            .filter(playerTrainingEvent -> playerTrainingEvent.getPlayerId().equals(Player.PlayerId.of(playerId)))
            .toList();

        return ResponseEntity.ok(new PlayerHistoricalTrainingResponse()
            .playerId(playerId)
            .trainings(trainings.stream()
                .map(PlayerTrainingResponseMapper.INSTANCE::fromPlayerTrainingEvent)
                .toList()));
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
        switch (registerInvestmentOnCanteraRequest.getInvestment()) {
            case ECONOMY:
                canteraEconomyInvestmentUsecase.invest(id, registerInvestmentOnCanteraRequest.getPoints());
                break;
            case TRADITION:
                canteraTraditionInvestmentUsecase.invest(id, registerInvestmentOnCanteraRequest.getPoints());
                break;
            case BUILDING:
                canteraBuildingsInvestmentUsecase.invest(id, registerInvestmentOnCanteraRequest.getPoints());
                break;
            default:
                throw new IllegalArgumentException("Invalid investment type");
        }
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
                weeklyIncomeSponsorUsecase.income(Team.TeamId.of(teamId), mode, wins);
            }));

        int years = weeks / 13; // 13 weeks in a season
        IntStream.rangeClosed(1, years).forEach(index -> registerSponsorIncomeRequest.getSponsors()
            .stream()
            .filter(sponsor -> SponsorPeriodicity.ANNUAL.equals(sponsor.getPeriodicity()))
            .forEach(sponsor -> {
                Team.Economy.IncomeMode mode = Team.Economy.IncomeMode.valueOf(sponsor.getMode().name());
                annualIncomeSponsorUsecase.income(Team.TeamId.of(teamId), mode, wins);
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
    public ResponseEntity<Void> simulateSalaryIncrease(String teamId) {
        updateSalariesTeamUseCase.update(Team.TeamId.of(teamId));
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> simulateAttendanceIncome(String teamId, RegisterAttendanceIncomeRequest registerAttendanceIncomeRequest) {

        Integer spectators = registerAttendanceIncomeRequest.getSpectators();
        Double seatPrice = registerAttendanceIncomeRequest.getSeatPrice();

        matchIncomeAttendanceUseCase.income(Team.TeamId.of(teamId), spectators, seatPrice);

        return ResponseEntity.ok().build();
    }

}
