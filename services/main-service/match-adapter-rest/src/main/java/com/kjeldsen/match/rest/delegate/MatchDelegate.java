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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
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
     * The match service uses a different internal representation of teams and
     * players so here
     * these entities are mapped from the database version to the engine version,
     * which also
     * includes modifiers that differ for each match (these are passed as match
     * creation params).
     */

    /*********************** MATCH TEAM START ***********************/

    @Override
    public ResponseEntity<TeamResponse> getMatchTeam(String teamId, String matchId) {
        GetMatchTeamUseCase.MatchAndTeam matchAndTeam = getMatchTeamUseCase.getMatchAndTeam(matchId, teamId);
        TeamResponse response = TeamMapper.INSTANCE.map(matchAndTeam.team());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> updateMatchTeam(String teamId, String matchId,
            EditMatchTeamRequest editMatchTeamRequest) {
        List<UpdateMatchLineupUseCase.PlayerUpdateDTO> players = editMatchTeamRequest.getPlayers().stream()
                .map(player -> UpdateMatchLineupUseCase.PlayerUpdateDTO.builder()
                        .id(player.getId())
                        .playerOrder(
                                player.getPlayerOrder() != null
                                        ? com.kjeldsen.player.domain.PlayerOrder.valueOf(player.getPlayerOrder().name())
                                        : null)
                        .position(player.getPosition() != null ? PlayerPosition.valueOf(player.getPosition().name())
                                : null)
                        .playerOrderDestinationPitchArea(player.getPlayerOrderDestinationPitchArea() != null
                                ? com.kjeldsen.player.domain.PitchArea
                                        .valueOf(player.getPlayerOrderDestinationPitchArea().name())
                                : null)
                        .status(PlayerStatus.valueOf(player.getStatus().name()))
                        .build())
                .toList();

        com.kjeldsen.match.domain.modifers.TeamModifiers teamModifiers = TeamMapper.INSTANCE
                .map(editMatchTeamRequest.getTeamModifiers());

        // Special use case when you're challenging your self
        if (editMatchTeamRequest.getSelf() != null && editMatchTeamRequest.getSelf()) {
            Match match = getMatchUseCase.get(matchId);
            updateMatchLineupUseCase.updateSelf(teamId, match, players, teamModifiers);
        } else {
            updateMatchLineupUseCase.update(matchId, teamId, players, teamModifiers);
        }
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
                    .map(p -> UpdateMatchLineupUseCase.buildPlayer(p, matchAndTeam.team().getRole())).toList();
        } else {
            players = Stream.concat(matchAndTeam.team().getPlayers().stream(), matchAndTeam.team().getBench().stream()).toList();
        }

        TeamFormationValidationResult validationResult = TeamFormationValidator.validate(players);
        String response = JsonUtils.prettyPrint(validationResult);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*********************** MATCH LINEUP END ***********************/

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

    // TODO REWORK TO USE THE SecurityUtils.getCurrentUserId(); instead of
    // specifying the user
    @Override
    public ResponseEntity<List<MatchResponse>> getAllMatches(String teamId, String leagueId, Integer size,
            Integer page) {
        List<Match> matches = getMatchUseCase.getAll(teamId, leagueId);
        List<MatchResponse> response = matches.stream()
                .map(MatchMapper.INSTANCE::map)
                .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MatchResponse> getMatch(String matchId) {
        log.info("getMatchTeam(matchId={})", matchId);
        Match match = getMatchUseCase.get(matchId);
        MatchResponse response = MatchMapper.INSTANCE.map(match);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
