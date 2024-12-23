package com.kjeldsen.player.rest.delegate;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.player.application.usecases.GetTeamUseCase;
import com.kjeldsen.player.application.usecases.economy.*;
import com.kjeldsen.player.application.usecases.player.UpdateTeamPlayersUseCase;
import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.PlayerOrder;
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
import java.util.stream.Collectors;

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

    // Team
    private final UpdateTeamPlayersUseCase updateTeamPlayersUseCase;

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
        List<UpdateTeamPlayersUseCase.PlayerEdit> playerEdits = playerUpdates.stream().map(update -> new UpdateTeamPlayersUseCase.PlayerEdit(
                update.getId(),
                PlayerStatus.valueOf(update.getStatus().name()),
                PlayerPosition.valueOf(update.getPosition().name()),
                PlayerOrder.valueOf(update.getPlayerOrder().name())
            ))
            .collect(Collectors.toList());
        Team team = updateTeamPlayersUseCase.update(playerEdits, teamId);
        TeamResponse response = TeamMapper.INSTANCE.map(team);
        return ResponseEntity.ok(response);
    }


    private PlayerStatus extractStatus(EditPlayerRequest playerUpdate) {
        return PlayerStatus.valueOf(playerUpdate.getStatus().getValue());
    }
}
