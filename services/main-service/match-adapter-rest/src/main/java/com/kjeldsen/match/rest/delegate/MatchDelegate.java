package com.kjeldsen.match.rest.delegate;

import com.kjeldsen.match.Game;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Match.Status;
import com.kjeldsen.match.entities.MatchReport;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.modifers.HorizontalPressure;
import com.kjeldsen.match.modifers.Tactic;
import com.kjeldsen.match.modifers.VerticalPressure;
import com.kjeldsen.match.rest.api.MatchApiDelegate;
import com.kjeldsen.match.rest.model.ActionResponse;
import com.kjeldsen.match.rest.model.CreateMatchRequest;
import com.kjeldsen.match.rest.model.DuelResponse;
import com.kjeldsen.match.rest.model.DuelResultResponse;
import com.kjeldsen.match.rest.model.EditMatchRequest;
import com.kjeldsen.match.rest.model.MatchReportResponse;
import com.kjeldsen.match.rest.model.MatchResponse;
import com.kjeldsen.match.rest.model.Modifiers;
import com.kjeldsen.match.rest.model.PlayResponse;
import com.kjeldsen.match.rest.model.PlayerPositionResponse;
import com.kjeldsen.match.rest.model.PlayerResponse;
import com.kjeldsen.match.rest.model.TeamResponse;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.match.utils.JsonUtils;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerStatus;
import com.kjeldsen.player.domain.Team.TeamId;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class MatchDelegate implements MatchApiDelegate {

    private final TeamReadRepository teamRepo;
    private final PlayerReadRepository playerRepo;
    private final MatchRepository matchRepo;

    /*
     * The match service uses a different internal representation of teams and players so here
     * these entities are mapped from the database version to the engine version, which also
     * includes modifiers that differ for each match (these are passed as match creation params).
     */

    @Override
    public ResponseEntity<Void> createMatch(CreateMatchRequest request) {
        TeamId homeId = TeamId.of(request.getHome().getId());
        com.kjeldsen.player.domain.Team home = teamRepo.findById(homeId)
                .orElseThrow(() -> new RuntimeException("Home team not found"));

        TeamId awayId = TeamId.of(request.getAway().getId());
        com.kjeldsen.player.domain.Team away = teamRepo.findById(awayId)
                .orElseThrow(() -> new RuntimeException("Away team not found"));

        Team engineHome = buildTeam(home, request.getHome().getModifiers());
        Team engineAway = buildTeam(away, request.getAway().getModifiers());

        Match match = Match.builder()
                .id(java.util.UUID.randomUUID().toString())
                .home(engineHome)
                .away(engineAway)
                .dateTime(request.getDateTime())
                .status(Status.PENDING)
                .build();

        matchRepo.save(match);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> editMatch(String matchId, EditMatchRequest request) {
        Match match = matchRepo.findOneById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        if (match.getStatus() != Status.PENDING) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Status status = Status.valueOf(request.getStatus().getValue());
        match.setStatus(status);

        // TODO - if challenge request is accepted schedule the match to be played at the given date
        //  time, but for now just play it immediately

        if (status == Status.ACCEPTED) {
            GameState state = Game.play(match);
            MatchReport report = new MatchReport(state, state.getPlays(), match.getHome(),
                    match.getAway());
            match.setMatchReport(report);
            matchRepo.save(match);

            String json = JsonUtils.exclude(
                    report, "plays.duel.initiator.skills", "plays.duel.challenger.skills");
            return ResponseEntity.ok().body(json);
        }

        matchRepo.save(match);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<MatchResponse>> getAllMatches(String teamId, Integer size,
                                                             Integer page) {
        List<Match> matches = matchRepo.findMatchesByTeamId(teamId);

        List<MatchResponse> response = matches.stream()
                .map(match -> {

                    MatchResponse res = new MatchResponse();
                    res.setId(match.getId());
                    res.setDateTime(match.getDateTime());

                    TeamResponse resHomeTeam = buildMatchTeamResponse(match.getHome());
                    res.setHome(resHomeTeam);

                    TeamResponse resAwayTeam = buildMatchTeamResponse(match.getAway());
                    res.setAway(resAwayTeam);

                    return res;
                })
                .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<MatchResponse> getMatch(String matchId) {
        Match match = matchRepo.findOneById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        MatchResponse response = this.buildMatchResponse(match);

        if (match.getMatchReport() != null) {
            MatchReportResponse report = this.buildMatchReportResponse(match.getMatchReport());
            response.setMatchReport(report);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private MatchReportResponse buildMatchReportResponse(MatchReport matchReport) {
        MatchReportResponse result = new MatchReportResponse();

        result.setHomeScore(matchReport.getHomeScore());
        result.setAwayScore(matchReport.getAwayScore());
        result.setHome(this.buildMatchTeamResponse(matchReport.getHome()));
        result.setAway(this.buildMatchTeamResponse(matchReport.getAway()));
        List<PlayResponse> playsResponse = matchReport.getPlays().stream().map(play -> {
            PlayResponse res = new PlayResponse();
            res.setClock(play.getClock());
            res.setAction(ActionResponse.valueOf(play.getAction().name()));

            DuelResponse duelResponse = new DuelResponse();

            duelResponse.setResult(DuelResultResponse.valueOf(play.getDuel().getResult().name()));
            PlayerResponse initiator = this.buildPlayerResponse(play.getDuel().getInitiator());
            duelResponse.setInitiator(initiator);

            if (play.getDuel().getChallenger() != null) {
                PlayerResponse challenger = this.buildPlayerResponse(play.getDuel().getChallenger());
                duelResponse.setChallenger(challenger);
            }

            if (play.getDuel().getReceiver() != null) {
                PlayerResponse receiver = this.buildPlayerResponse(play.getDuel().getReceiver());
                duelResponse.setReceiver(receiver);
            }

            res.setDuel(duelResponse);

            return res;
        }).toList();
        result.setPlays(playsResponse);

        return result;
    }

    private MatchResponse buildMatchResponse(Match match) {
        MatchResponse res = new MatchResponse();

        res.setId(match.getId());
        res.setDateTime(match.getDateTime());
        res.setStatus(com.kjeldsen.match.rest.model.Status.valueOf(match.getStatus().name()));

        TeamResponse resHomeTeam = buildMatchTeamResponse(match.getHome());
        res.setHome(resHomeTeam);

        TeamResponse resAwayTeam = buildMatchTeamResponse(match.getAway());
        res.setAway(resAwayTeam);

        return res;
    }

    private TeamResponse buildMatchTeamResponse(Team matchTeam) {
        TeamResponse res = new TeamResponse();

        res.setId(matchTeam.getId());

        TeamId teamId = TeamId.of(matchTeam.getId());
        com.kjeldsen.player.domain.Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        res.setName(team.getName());

        List<PlayerResponse> teamPlayers = matchTeam.getPlayers().stream().map(this::buildPlayerResponse).collect(Collectors.toList());
        res.setPlayers(teamPlayers);

        if (matchTeam.getBench() != null) {
            List<PlayerResponse> benchPlayers = matchTeam.getBench().stream().map(this::buildPlayerResponse).collect(Collectors.toList());
            res.setBench(benchPlayers);
        }

        res.setModifiers(new Modifiers());
        if (matchTeam.getTactic() != null) {
            res.getModifiers().setTactic(com.kjeldsen.match.rest.model.Tactic.valueOf(matchTeam.getTactic().name()));
        }
        if (matchTeam.getHorizontalPressure() != null) {
            res.getModifiers().setHorizontalPressure(com.kjeldsen.match.rest.model.HorizontalPressure.valueOf(matchTeam.getHorizontalPressure().name()));
        }

        if (matchTeam.getVerticalPressure() != null) {
            res.getModifiers().setVerticalPressure(com.kjeldsen.match.rest.model.VerticalPressure.valueOf(matchTeam.getVerticalPressure().name()));
        }

        return res;
    }

    private PlayerResponse buildPlayerResponse(Player player) {
        PlayerResponse result = new PlayerResponse();
        result.setId(player.getId());
        result.setName(player.getName());
        result.setTeamId(player.getTeamId());
        result.setPosition(PlayerPositionResponse.valueOf(player.getPosition().name()));
        return result;
    }

    private Team buildTeam(com.kjeldsen.player.domain.Team home, Modifiers modifiers) {
        List<Player> activePlayers = playerRepo.findByTeamId(home.getId()).stream()
                .filter(player -> player.getStatus() == PlayerStatus.ACTIVE)
                .map(this::buildPlayer)
                .toList();

        List<Player> benchPlayers = playerRepo.findByTeamId(home.getId()).stream()
                .filter(player -> player.getStatus() == PlayerStatus.BENCH)
                .map(this::buildPlayer)
                .toList();

        return Team.builder()
                .id(home.getId().value())
                .players(activePlayers)
                .bench(benchPlayers)
                .tactic(Tactic.valueOf(modifiers.getTactic().getValue()))
                .verticalPressure(VerticalPressure.valueOf(modifiers.getVerticalPressure().getValue()))
                .horizontalPressure(
                        HorizontalPressure.valueOf(modifiers.getHorizontalPressure().getValue()))
                .build();
    }

    private Player buildPlayer(com.kjeldsen.player.domain.Player player) {
        Map<PlayerSkill, Integer> skills = player.getActualSkills().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getActual()));
        skills.put(PlayerSkill.INTERCEPTING, 0);

        return Player.builder()
                .id(player.getId().value())
                .name(player.getName())
                .status(player.getStatus())
                .teamId(player.getTeamId().value())
                .position(player.getPosition())
                .skills(skills)
                .playerOrder(player.getPlayerOrder())
                .build();
    }
}
