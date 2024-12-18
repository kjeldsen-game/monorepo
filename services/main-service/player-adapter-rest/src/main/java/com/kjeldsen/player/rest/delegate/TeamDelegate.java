package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.application.usecases.economy.*;
import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerStatus;
import com.kjeldsen.player.domain.Team.TeamId;
import com.kjeldsen.player.domain.repositories.FindTeamsQuery;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.rest.api.TeamApiDelegate;
import com.kjeldsen.player.rest.mapper.EconomyMapper;
import com.kjeldsen.player.rest.mapper.PlayerMapper;
import com.kjeldsen.player.rest.mapper.TeamMapper;
import com.kjeldsen.player.rest.model.*;

import java.util.*;

import com.kjeldsen.player.rest.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
public class TeamDelegate implements TeamApiDelegate {
    // Common
    private final GetTeamUseCase getTeamUseCase;

    private final GetTeamTransactionsUseCase getTeamTransactionsUseCase;
    private final TeamReadRepository teamReadRepository;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;

    // Economy
    private final SignSponsorIncomeUseCase signSponsorIncomeUseCase;
    private final SignBillboardIncomeUseCase signBillboardIncomeUseCase;
    private final UpdateTeamPricingUsecase updateTeamPricingUseCase;
    private final GetPlayerWagesTransactionsUseCase getPlayerWagesTransactionsUseCase;


    /***************************** ECONOMY REST API *****************************/
    @Override
    public ResponseEntity<Void> updatePricing(String teamId, UpdatePricingRequest updatePricingRequest) {
        // Access denied as the path teamId is different that the teamId from Token
        if (!Objects.equals(getTeamUseCase.get(SecurityUtils.getCurrentUserId()).getId().value(), teamId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        updatePricingRequest.getPrices().forEach(price -> {
            updateTeamPricingUseCase.update(Team.TeamId.of(teamId), price.getValue(),
                Team.Economy.PricingType.valueOf(price.getType().name()));
        });
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> signBillboard(String teamId, SignBillboardRequest signBillboardRequest) {
        // Access denied as the path teamId is different that the teamId from Token
        if (!Objects.equals(getTeamUseCase.get(SecurityUtils.getCurrentUserId()).getId().value(), teamId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Team.Economy.BillboardIncomeType mode = Team.Economy.BillboardIncomeType.valueOf(
            signBillboardRequest.getMode().name());
        signBillboardIncomeUseCase.sign(TeamId.of(teamId), mode);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> signSponsor(String teamId, SignSponsorRequest signSponsorRequest) {
        // Access denied as the path teamId is different that the teamId from Token
        if (!Objects.equals(getTeamUseCase.get(SecurityUtils.getCurrentUserId()).getId().value(), teamId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        signSponsorIncomeUseCase.sign(TeamId.of(teamId),
            Team.Economy.IncomePeriodicity.valueOf(signSponsorRequest.getPeriodicity().name()),
            Team.Economy.IncomeMode.valueOf(signSponsorRequest.getMode().name()));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<EconomyResponse> getTeamEconomy(String teamId) {
        // Access denied as the path teamId is different that the teamId from Token
        if (!Objects.equals(getTeamUseCase.get(SecurityUtils.getCurrentUserId()).getId().value(), teamId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Team team = teamReadRepository.findById(TeamId.of(teamId)).orElse(null);

        Map<String, GetTransactionsUseCaseAbstract.TransactionSummary> transactions =
            getTeamTransactionsUseCase.getTransactions(teamId);
        List<GetPlayerWagesTransactionsUseCase.PlayerWageSummary> playerWageSummaryList =
            getPlayerWagesTransactionsUseCase.getPlayerWagesTransactions(teamId);

        List<Transaction> responseTransactions = transactions.entrySet().stream()
            .map(EconomyMapper.INSTANCE::mapToTransaction)
            .toList();

        List<PlayerWageTransaction> responseWages = playerWageSummaryList.stream()
            .map(EconomyMapper.INSTANCE::mapToPlayerWageTransaction).toList();

        List<Sponsor> sponsors = team.getEconomy().getSponsors().entrySet().stream()
            .map(entry -> new Sponsor()
                .mode(entry.getValue() != null ? SponsorMode.valueOf(entry.getValue().name()) : null)
                .periodicity(SponsorPeriodicity.valueOf(entry.getKey().name())))
            .toList();

        List<PriceItem> prices = team.getEconomy().getPrices().entrySet().stream()
            .filter(entry -> entry.getKey() != null && entry.getValue() != null)
            .map(entry -> new PriceItem()
                .type(PricingType.valueOf(entry.getKey().name()))
                .value(entry.getValue())).toList();

        BillboardDeal billboardDeal = EconomyMapper.INSTANCE.mapToBillboardDeal(team.getEconomy().getBillboardDeal());

        EconomyResponse economyResponse = new EconomyResponse()
            .playerWages(responseWages)
            .balance(team.getEconomy().getBalance())
            .prices(prices)
            .transactions(responseTransactions)
            .billboardDeal(billboardDeal)
            .sponsor(sponsors);

        return ResponseEntity.ok(economyResponse);
    }

    @Override
    public ResponseEntity<List<TeamResponse>> getAllTeams(String name, Integer size, Integer page) {
        FindTeamsQuery query = FindTeamsQuery.builder()
            .name(name)
            .size(size)
            .page(page)
            .build();
        List<Team> teams = teamReadRepository.find(query);
        List<TeamResponse> response = teams.stream().map(TeamMapper.INSTANCE::map).toList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TeamResponse> getTeamById(String teamId) {
        // Different user than the team owner is accessing the team
        Team team = teamReadRepository.findOneById(Team.TeamId.of(teamId))
            .orElseThrow();
        TeamResponse response = TeamMapper.INSTANCE.map(team);
        if (!Objects.equals(getTeamUseCase.get(SecurityUtils.getCurrentUserId()).getId().value(), teamId)) {
            // TODO filter the response if other team access remove the tactics only show players
        }

        playerReadRepository.findByTeamId(TeamId.of(teamId));
        List<PlayerResponse> players = playerReadRepository.findByTeamId(TeamId.of(teamId))
            .stream()
            .map(PlayerMapper.INSTANCE::playerResponseMap)
            .toList();
        response.setPlayers(players);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ResponseEntity<TeamResponse> updateTeamById(String teamId,
        EditTeamRequest editTeamRequest) {
        // Access denied as the path teamId is different that the teamId from Token
        if (!Objects.equals(getTeamUseCase.get(SecurityUtils.getCurrentUserId()).getId().value(), teamId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<EditPlayerRequest> playerUpdates = editTeamRequest.getPlayers();

        List<EditPlayerRequest> activePlayers = playerUpdates.stream()
            .filter(playerUpdate -> extractStatus(playerUpdate) == PlayerStatus.ACTIVE)
            .toList();
        if (activePlayers.size() != 11) {
            throw new InvalidTeamException("11 active players are required");
        }
        validateTeamFormation(activePlayers);

        List<Player> players = playerReadRepository.findByTeamId(TeamId.of(teamId));
        playerUpdates.forEach(update -> {
            Player player = players.stream().filter(p ->
                Objects.equals(p.getId().value(), update.getId())).findAny().orElseThrow();

            boolean playerChangingPositionFromGoalkeeper = PlayerPosition.GOALKEEPER.equals(player.getPosition()) && !PlayerPosition.GOALKEEPER.equals(PlayerPosition.valueOf(update.getPosition().getValue()));
            boolean playerChangingPositionToGoalkeeper = !PlayerPosition.GOALKEEPER.equals(player.getPosition()) && PlayerPosition.GOALKEEPER.equals(PlayerPosition.valueOf(update.getPosition().getValue()));
            if (playerChangingPositionFromGoalkeeper || playerChangingPositionToGoalkeeper) throw new RuntimeException(player.getName() + ": GOALKEEPER position is not changeable.");

            player.setPosition(PlayerPosition.valueOf(update.getPosition().name()));
            player.setStatus(PlayerStatus.valueOf(update.getStatus().name()));
            playerWriteRepository.save(player);
        });

        // TODO bench players - for now set every one else as inactive since bench is not required.
        //   These players may have been set as active for a previous match.
        players.forEach(player -> {
            boolean isNotSetActive = activePlayers.stream()
                .noneMatch(active -> Objects.equals(active.getId(), player.getId().value()));
            player.setStatus(PlayerStatus.INACTIVE);
            if (isNotSetActive) {
                playerWriteRepository.save(player);
            }
        });

        Team team = teamReadRepository.findOneById(Team.TeamId.of(teamId)).orElseThrow();
        TeamResponse response = TeamMapper.INSTANCE.map(team);
        return ResponseEntity.ok(response);
    }


    // Validates that the 11 players on the active team conform to the rules about positions
    private void validateTeamFormation(List<EditPlayerRequest> activePlayers) {
        List<EditPlayerRequest> goalkeepers = activePlayers.stream()
            .filter(playerUpdate -> extractPosition(playerUpdate) == PlayerPosition.GOALKEEPER)
            .toList();

        if (goalkeepers.size() != 1) {
            throw new InvalidTeamException("A goalkeeper is required");
        }

        validateDefenders(activePlayers);
        validateMidfielders(activePlayers);
        validateAttackers(activePlayers);
        validateBackFlankCoverage(activePlayers);
        validateMidfieldFlankCoverage(activePlayers);
    }

    private void validateBackFlankCoverage(List<EditPlayerRequest> activePlayers) {
        boolean leftDefender = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.LEFT_BACK::equals);
        boolean leftWingBack = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.LEFT_WINGBACK::equals);
        if (!leftDefender && !leftWingBack) {
            throw new InvalidTeamException("Back left flank coverage is required (LB or LWB)");
        }
        if (leftDefender && leftWingBack) {
            throw new InvalidTeamException(
                "Only one player covering back left flank is allowed (LB or LWB)");
        }

        boolean rightDefender = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.RIGHT_BACK::equals);
        boolean rightWingback = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.RIGHT_WINGBACK::equals);
        if (!rightDefender && !rightWingback) {
            throw new InvalidTeamException("Back right flank coverage is required (RB or RWB)");
        }
        if (rightDefender && rightWingback) {
            throw new InvalidTeamException(
                "Only one player covering back right flank is allowed (RB or RWB)");
        }
    }

    private void validateMidfieldFlankCoverage(List<EditPlayerRequest> activePlayers) {
        boolean leftMidfielder = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.LEFT_MIDFIELDER::equals);
        boolean leftWinger = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.LEFT_WINGER::equals);
        if (!leftMidfielder && !leftWinger) {
            throw new InvalidTeamException("Left flank coverage is required (LM or LW)");
        }
        if (leftMidfielder && leftWinger) {
            throw new InvalidTeamException(
                "Only one player covering midfield left flank is allowed (LM or LW)");
        }

        boolean rightMidfielder = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.RIGHT_MIDFIELDER::equals);
        boolean rightWinger = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.RIGHT_WINGER::equals);
        if (!rightMidfielder && !rightWinger) {
            throw new InvalidTeamException("Right flank coverage is required (RM or RW)");
        }
        if (rightMidfielder && rightWinger) {
            throw new InvalidTeamException(
                "Only one player covering right midfield flank is allowed (RM or RW)");
        }
    }

    private void validateAttackers(List<EditPlayerRequest> activePlayers) {
        List<EditPlayerRequest> forwards = activePlayers.stream()
            .filter(playerUpdate -> extractPosition(playerUpdate) == PlayerPosition.FORWARD
                || extractPosition(playerUpdate) == PlayerPosition.AERIAL_FORWARD)
            .toList();

        if (forwards.isEmpty()) {
            throw new InvalidTeamException("A minimum of 1 forward is required");
        }

        if (forwards.size() > 3) {
            throw new InvalidTeamException("A maximum of 3 forwards are allowed");
        }

        List<EditPlayerRequest> strikers = activePlayers.stream()
            .filter(playerUpdate -> extractPosition(playerUpdate) == PlayerPosition.STRIKER
                || extractPosition(playerUpdate) == PlayerPosition.AERIAL_STRIKER)
            .toList();

        if (strikers.size() > 1) {
            throw new InvalidTeamException("Only 1 striker is allowed");
        }
    }

    private void validateMidfielders(List<EditPlayerRequest> activePlayers) {
        List<EditPlayerRequest> midfielders = activePlayers.stream()
            .filter(playerUpdate -> extractPosition(playerUpdate).isMidfielder())
            .toList();

        if (midfielders.size() < 3) {
            throw new InvalidTeamException("A minimum of 3 midfielders are required");
        }

        if (midfielders.size() > 6) {
            throw new InvalidTeamException("A maximum of 6 midfielders are allowed");
        }

        if (midfielders.stream().noneMatch(midfielder -> extractPosition(midfielder).isCentral())) {
            throw new InvalidTeamException("At least one central midfielder is required");
        }

        List<EditPlayerRequest> defensiveMidfielders = midfielders.stream()
            .filter(
                midfielder -> extractPosition(midfielder) == PlayerPosition.DEFENSIVE_MIDFIELDER)
            .toList();
        if (defensiveMidfielders.size() > 1) {
            throw new InvalidTeamException("Only one defensive midfielder is allowed");
        }

        List<EditPlayerRequest> offensiveMidfielders = midfielders.stream()
            .filter(
                midfielder -> extractPosition(midfielder) == PlayerPosition.OFFENSIVE_MIDFIELDER)
            .toList();
        if (offensiveMidfielders.size() > 1) {
            throw new InvalidTeamException("Only one offensive midfielder is allowed");
        }
    }

    private void validateDefenders(List<EditPlayerRequest> activePlayers) {
        List<EditPlayerRequest> defenders = activePlayers.stream()
            .filter(playerUpdate -> extractPosition(playerUpdate).isDefender()
                || extractPosition(playerUpdate).isWingback())
            .toList();

        if (defenders.size() < 3) {
            throw new InvalidTeamException("A minimum of 3 defenders are required");
        }
        if (defenders.size() > 5) {
            throw new InvalidTeamException("A maximum of 5 defenders are allowed");
        }

        List<EditPlayerRequest> centralDefenders = defenders.stream()
            .filter(defender -> extractPosition(defender).isCentral())
            .toList();

        if (centralDefenders.size() > 3) {
            throw new InvalidTeamException("A maximum of 3 central defenders are allowed");
        }

        List<EditPlayerRequest> sweepers = activePlayers.stream()
            .filter(playerUpdate -> extractPosition(playerUpdate) == PlayerPosition.SWEEPER)
            .toList();

        if (sweepers.size() > 1) {
            throw new InvalidTeamException("Only one sweeper is allowed");
        }
    }

    private PlayerPosition extractPosition(EditPlayerRequest playerUpdate) {
        return PlayerPosition.valueOf(playerUpdate.getPosition().getValue());
    }

    private PlayerStatus extractStatus(EditPlayerRequest playerUpdate) {
        return PlayerStatus.valueOf(playerUpdate.getStatus().getValue());
    }
}
