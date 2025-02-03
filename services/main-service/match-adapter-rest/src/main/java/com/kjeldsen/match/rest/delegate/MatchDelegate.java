package com.kjeldsen.match.rest.delegate;

import com.kjeldsen.match.application.usecases.*;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Match.Status;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import com.kjeldsen.match.rest.api.MatchApiDelegate;
import com.kjeldsen.match.rest.mapper.MatchMapper;
import com.kjeldsen.match.rest.mapper.TeamMapper;
import com.kjeldsen.match.rest.model.*;
import com.kjeldsen.match.domain.utils.JsonUtils;
import com.kjeldsen.match.domain.validation.TeamFormationValidationResult;
import com.kjeldsen.match.domain.validation.TeamFormationValidator;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerStatus;
import com.kjeldsen.player.domain.Team.TeamId;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class MatchDelegate implements MatchApiDelegate {

    private final PlayerReadRepository playerRepo;
    private final MatchWriteRepository matchWriteRepository;

    private final GetMatchTeamUseCase getMatchTeamUseCase;
    private final UpdateMatchLineupUseCase updateMatchLineupUseCase;

    private final CreateMatchUseCase createMatchUseCase;
    private final GetMatchUseCase getMatchUseCase;
    private final UpdateMatchChallengeUseCase updateMatchChallengeUseCase;
    private final ExecuteMatchUseCase executeMatchUseCase;
    /*
     * The match service uses a different internal representation of teams and players so here
     * these entities are mapped from the database version to the engine version, which also
     * includes modifiers that differ for each match (these are passed as match creation params).
     */

    /*********************** MATCH TEAM START  ***********************/

    @Override
    public ResponseEntity<TeamResponse> getMatchTeam(String teamId, String matchId) {
        GetMatchTeamUseCase.MatchAndTeam matchAndTeam = getMatchTeamUseCase.getMatchAndTeam(teamId, matchId);
        TeamResponse response = TeamMapper.INSTANCE.map(matchAndTeam.team());
       return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> updateMatchTeam(String teamId, String matchId, EditMatchTeamRequest editMatchTeamRequest) {
        List<UpdateMatchLineupUseCase.PlayerUpdateDTO> players = editMatchTeamRequest.getPlayers().stream()
            .map(player -> UpdateMatchLineupUseCase.PlayerUpdateDTO.builder()
                .id(player.getId())
                .playerOrder(
                    player.getPlayerOrder() != null
                        ? com.kjeldsen.player.domain.PlayerOrder.valueOf(player.getPlayerOrder().name())
                        : null
)                .position(PlayerPosition.valueOf(player.getPosition().name()))
                .status(PlayerStatus.valueOf(player.getStatus().name()))
                .build())
            .toList();
        com.kjeldsen.match.domain.modifers.TeamModifiers teamModifiers = TeamMapper.INSTANCE.map(editMatchTeamRequest.getTeamModifiers());
        updateMatchLineupUseCase.update(matchId, teamId, players, teamModifiers);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<String> validate(String matchId, String teamId) {
        List<Player> players;
        GetMatchTeamUseCase.MatchAndTeam matchAndTeam = getMatchTeamUseCase.getMatchAndTeam(matchId, teamId);
        // get the lineup
        if (!matchAndTeam.team().getSpecificLineup()) {
            List<com.kjeldsen.player.domain.Player> playersDomain = playerRepo.findByTeamId(TeamId.of(teamId));
            players = playersDomain.stream()
                .map(p -> updateMatchLineupUseCase.buildPlayer(p, matchAndTeam.team().getRole())).toList();
        } else {
            players = Stream.concat(matchAndTeam.team().getPlayers().stream(), matchAndTeam.team().getBench().stream())
                .collect(Collectors.toList());
        }

        TeamFormationValidationResult validationResult = TeamFormationValidator.validate(players);
        String response = JsonUtils.prettyPrint(validationResult);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*********************** MATCH LINEUP END  ***********************/

    @Override
    public ResponseEntity<Void> createMatch(CreateMatchRequest request) {
        createMatchUseCase.create(request.getHome().getId(),
            request.getAway().getId(), request.getDateTime(), null);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> editMatch(String matchId, EditMatchRequest request) {
        // For challenges after the match is accepted
        if (request.getStatus().name().equals(Status.SCHEDULED.name())) {
            executeMatchUseCase.execute(matchId);
        } else {
            updateMatchChallengeUseCase.update(matchId, Status.valueOf(request.getStatus().name()));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO REWORK TO USE THE SecurityUtils.getCurrentUserId(); instead of specifying the user
    @Override
    public ResponseEntity<List<MatchResponse>> getAllMatches(String teamId, String leagueId , Integer size,
        Integer page) {
        List<Match> matches = getMatchUseCase.getAll(teamId, leagueId);
        List<MatchResponse> response = matches.stream()
            .map(MatchMapper.INSTANCE::map)
            .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<MatchResponse> getMatch(String matchId) {
        Match match = getMatchUseCase.get(matchId);
        MatchResponse response = MatchMapper.INSTANCE.map(match);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // TODO I dont think this is used in FE can it be removed?
    // TODO REWORK TO USE THE SecurityUtils.getCurrentUserId(); instead of specifying the user
    @Override
    public ResponseEntity<Void> addPlayer(String matchId,
        String teamId,
        EditPlayerRequest playerRequest) {

        Match match = getMatchUseCase.get(matchId);

        Team team = match.getHome().getId().equals(teamId)
            ? match.getHome()
            : match.getAway().getId().equals(teamId)
            ? match.getAway()
            : null;
        if (team == null) {
            throw new RuntimeException("Team not found");
        }

        PlayerStatus requestStatus = PlayerStatus.valueOf(playerRequest.getStatus().getValue());

        if (PlayerStatus.INACTIVE.equals(requestStatus)) {
            throw new RuntimeException("Cannot add inactive player");
        }

        final String playerId = playerRequest.getId();

        Optional<Player> optPlayer = Stream.concat(team.getPlayers().stream(), team.getBench().stream())
            .filter(p -> p.getId().equals(playerId))
            .findAny();

        if (optPlayer.isEmpty()) {

            Optional<com.kjeldsen.player.domain.Player> optDomainPlayer = playerRepo.findOneById(
                com.kjeldsen.player.domain.Player.PlayerId.of(playerId));
            if (optDomainPlayer.isPresent()) {
                Player playerToAdd = updateMatchLineupUseCase.buildPlayer(optDomainPlayer.get(), team.getRole());
                playerToAdd.setPosition(PlayerPosition.valueOf(playerRequest.getPosition().getValue()));
                if (PlayerStatus.ACTIVE.equals(requestStatus)) {
                    team.getPlayers().add(playerToAdd);
                } else {
                    team.getBench().add(playerToAdd);
                }
            } else {
                throw new RuntimeException("Player does not exist");
            }

        } else {
            throw new RuntimeException("Player already part of the match");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> editPlayer(String matchId, String teamId, EditPlayerRequest playerRequest) {
        Match match = getMatchUseCase.get(matchId);

        Team team = match.getHome().getId().equals(teamId)
            ? match.getHome()
            : match.getAway().getId().equals(teamId)
            ? match.getAway()
            : null;
        if (team == null) {
            throw new RuntimeException("Team not found");
        }

        final String playerId = playerRequest.getId();

        Optional<Player> optPlayer = Stream.concat(team.getPlayers().stream(), team.getBench().stream())
            .filter(p -> p.getId().equals(playerId))
            .findAny();

        if (optPlayer.isPresent()) {
            Player player = optPlayer.get();

            if (playerRequest.getStatus() != null) {
                if (!player.getStatus().equals(PlayerStatus.valueOf(playerRequest.getStatus().getValue()))) {
                    if (PlayerStatus.INACTIVE.equals(PlayerStatus.valueOf(playerRequest.getStatus().getValue()))) {
                        // Players cannot be deactivated in lineup.
                        throw new RuntimeException(
                            "Inactive players should be removed from the lineup with delete."); // Changing status requires moving
                    }
                    if (PlayerStatus.ACTIVE.equals(PlayerStatus.valueOf(playerRequest.getStatus().getValue()))) {
                        // Moving player from bench to active.
                        team.getBench().removeIf(p -> p.getId().equals(playerId));
                        team.getPlayers().add(player);
                        player.setStatus(PlayerStatus.ACTIVE);
                    }
                    if (PlayerStatus.BENCH.equals(PlayerStatus.valueOf(playerRequest.getStatus().getValue()))) {
                        // Moving player from active to bench.
                        team.getPlayers().removeIf(p -> p.getId().equals(playerId));
                        team.getBench().add(player);
                        player.setStatus(PlayerStatus.BENCH);
                    }
                }
            }

            if (playerRequest.getPosition() != null) {
                player.setPosition(PlayerPosition.valueOf(playerRequest.getPosition().getValue()));
            }

            matchWriteRepository.save(match);

        } else {
            throw new RuntimeException("Player not found");
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Void> deletePlayer(String matchId,
        String teamId,
        String playerId) {

        Match match = getMatchUseCase.get(matchId);

        Team team = match.getHome().getId().equals(teamId)
            ? match.getHome()
            : match.getAway().getId().equals(teamId)
            ? match.getAway()
            : null;
        if (team == null) {
            throw new RuntimeException("Team not found");
        }

        Optional<Player> optPlayer = Stream.concat(team.getPlayers().stream(), team.getBench().stream())
            .filter(p -> p.getId().equals(playerId))
            .findAny();

        if (optPlayer.isPresent()) {
            team.getPlayers().removeIf(player -> player.getId().equals(playerId));
            team.getBench().removeIf(player -> player.getId().equals(playerId));

            matchWriteRepository.save(match);
        } else {
            throw new RuntimeException("Player not found");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
