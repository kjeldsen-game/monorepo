package com.kjeldsen.match.rest.delegate;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.match.Game;
import com.kjeldsen.match.entities.Action;
import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Match.Status;
import com.kjeldsen.match.entities.MatchReport;
import com.kjeldsen.match.entities.Player;
import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.entities.TeamRole;
import com.kjeldsen.match.modifers.HorizontalPressure;
import com.kjeldsen.match.modifers.Tactic;
import com.kjeldsen.match.modifers.VerticalPressure;
import com.kjeldsen.match.publisher.MatchEventPublisher;
import com.kjeldsen.match.repositories.MatchEventWriteRepository;
import com.kjeldsen.match.rest.api.MatchApiDelegate;
import com.kjeldsen.match.rest.model.*;
import com.kjeldsen.match.schedulers.MatchScheduler;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.match.utils.JsonUtils;
import com.kjeldsen.match.validation.TeamFormationValidationResult;
import com.kjeldsen.match.validation.TeamFormationValidator;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerStatus;
import com.kjeldsen.player.domain.Team.TeamId;
import com.kjeldsen.player.domain.events.MatchEvent;
import com.kjeldsen.player.domain.provider.InstantProvider;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
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

    private final TeamReadRepository teamRepo;
    private final PlayerReadRepository playerRepo;
    private final MatchRepository matchRepo;
    private final MatchEventPublisher matchEventPublisher;
    private final MatchEventWriteRepository matchEventWriteRepository;
    private final MatchScheduler matchScheduler;

    /*
     * The match service uses a different internal representation of teams and players so here
     * these entities are mapped from the database version to the engine version, which also
     * includes modifiers that differ for each match (these are passed as match creation params).
     */

    @Override
    public ResponseEntity<Void> createMatch(CreateMatchRequest request) {
        // TODO implement usage of the modifiers from the team
        TeamId homeId = TeamId.of(request.getHome().getId());
        com.kjeldsen.player.domain.Team home = teamRepo.findById(homeId)
            .orElseThrow(() -> new RuntimeException("Home team not found"));

        TeamId awayId = TeamId.of(request.getAway().getId());
        com.kjeldsen.player.domain.Team away = teamRepo.findById(awayId)
            .orElseThrow(() -> new RuntimeException("Away team not found"));

        Team engineHome = buildTeam(home, TeamRole.HOME, request.getHome().getModifiers());
        Team engineAway = buildTeam(away, TeamRole.AWAY, request.getAway().getModifiers());

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

//            matchScheduler.scheduleMatch(matchId, Instant.from(match.getDateTime()));

            // TODO remove this part once we start using scheduling
            GameState state = Game.play(match);
            Map<String, Integer> attendance = getMatchAttendance(match);
            MatchReport report = new MatchReport(state, state.getPlays(), match.getHome(),
                match.getAway(), attendance.get("homeAttendance"), attendance.get("awayAttendance"));

            match.setMatchReport(report);

            MatchEvent matchEvent = MatchEvent.builder()
                .id(EventId.generate())
                .occurredAt(InstantProvider.now())
                .matchId(match.getId())
                .homeTeamId(match.getHome().getId())
                .awayTeamId(match.getAway().getId())
                .homeScore(match.getMatchReport().getHomeScore())
                .awayScore(match.getMatchReport().getAwayScore())
                .homeAttendance(match.getMatchReport().getHomeAttendance())
                .awayAttendance(match.getMatchReport().getAwayAttendance())
                .build();

            matchEventWriteRepository.save(matchEvent);
            matchEventPublisher.publishMatchEvent(matchEvent);

            matchRepo.save(match);

            String json = JsonUtils.exclude(
                report, "plays.duel.initiator.skills", "plays.duel.challenger.skills");
            return ResponseEntity.ok().body(json);
        }

        matchRepo.save(match);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Map<String, Integer> getMatchAttendance(Match match) {
        com.kjeldsen.player.domain.Team homeTeam = teamRepo.findById(TeamId.of(match.getHome().getId()))
            .orElseThrow(() -> new RuntimeException("Team not found"));
        Integer capacity = homeTeam.getBuildings().getStadium().getSeats();
        int homeAttendance = Math.round(homeTeam.getFans().getTotalFans() * 0.8f);

        com.kjeldsen.player.domain.Team awayTeam = teamRepo.findById(TeamId.of(match.getAway().getId()))
            .orElseThrow(() -> new RuntimeException("Team not found"));
        Integer awayAttendance = awayTeam.getFans().getTotalFans();

        if (homeAttendance + awayAttendance > capacity) {
            float scaleFactor = (float) (homeAttendance + awayAttendance) / capacity;
            homeAttendance = Math.round(homeAttendance * scaleFactor);
            awayAttendance = Math.round(awayAttendance * scaleFactor);
        }
        return Map.of(
            "homeAttendance", homeAttendance,
            "awayAttendance", awayAttendance
        );
    }

    // TODO REWORK TO USE THE SecurityUtils.getCurrentUserId(); instead of specifying the user
    @Override
    public ResponseEntity<List<MatchResponse>> getAllMatches(String teamId, Integer size,
        Integer page) {
        List<Match> matches = matchRepo.findMatchesByTeamId(teamId);

        List<MatchResponse> response = matches.stream()
            .map(match -> {
                return this.buildMatchResponse(match);
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

    // TODO REWORK TO USE THE SecurityUtils.getCurrentUserId(); instead of specifying the user
    @Override
    public ResponseEntity<Void> addPlayer(String matchId,
        String teamId,
        EditPlayerRequest playerRequest) {

        Match match = matchRepo.findOneById(matchId)
            .orElseThrow(() -> new RuntimeException("Match not found"));

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
                Player playerToAdd = this.buildPlayer(optDomainPlayer.get(), team.getRole());
                playerToAdd.setPosition(PlayerPosition.valueOf(playerRequest.getPosition().getValue()));
                if (PlayerStatus.ACTIVE.equals(requestStatus)) {
                    team.getPlayers().add(playerToAdd);
                } else {
                    // Bench
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

    // TODO REWORK TO USE THE SecurityUtils.getCurrentUserId(); instead of specifying the user
    @Override
    public ResponseEntity<String> editPlayer(String matchId, String teamId, EditPlayerRequest playerRequest) {
        Match match = matchRepo.findOneById(matchId)
            .orElseThrow(() -> new RuntimeException("Match not found"));

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

            matchRepo.save(match);

        } else {
            throw new RuntimeException("Player not found");
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

    // TODO REWORK TO USE THE SecurityUtils.getCurrentUserId(); instead of specifying the user
    @Override
    public ResponseEntity<Void> deletePlayer(String matchId,
        String teamId,
        String playerId) {

        Match match = matchRepo.findOneById(matchId)
            .orElseThrow(() -> new RuntimeException("Match not found"));

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

            matchRepo.save(match);
        } else {
            throw new RuntimeException("Player not found");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO REWORK TO USE THE SecurityUtils.getCurrentUserId(); instead of specifying the user
    public ResponseEntity<String> validate(String matchId,
        String teamId) {

        Match match = matchRepo.findOneById(matchId)
            .orElseThrow(() -> new RuntimeException("Match not found"));

        Team team = null;
        if (match.getHome().getId().equals(teamId)) {
            team = match.getHome();
        } else if (match.getAway().getId().equals(teamId)) {
            team = match.getAway();
        } else {
            throw new RuntimeException("Team not found");
        }

        TeamFormationValidationResult validationResult = TeamFormationValidator.validate(team);

        String response = JsonUtils.prettyPrint(validationResult);

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
            res.awayScore(play.getAwayScore());
            res.homeScore(play.getHomeScore());
            DuelResponse duelResponse = new DuelResponse();
            duelResponse.setPitchArea(PitchArea.valueOf(play.getDuel().getPitchArea().name()));
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


            duelResponse.setInitiatorStats(buildPositionAssistanceStats(play.getDuel().getInitiatorStats(),
                play.getAction()));
            duelResponse.setChallengerStats(buildPositionAssistanceStats(play.getDuel().getChallengerStats(),
                play.getAction()));


            res.setDuel(duelResponse);

            return res;
        }).toList();
        result.setPlays(playsResponse);

        return result;
    }

    private DuelStats buildPositionAssistanceStats(com.kjeldsen.match.entities.DuelStats duelStats, Action action) {
        DuelStats result = new DuelStats();

        Optional.ofNullable(duelStats.getCarryover()).ifPresent(result::setCarryover);
        Optional.ofNullable(duelStats.getAssistance()).ifPresent(result::setAssistance);
        result.setTotal(duelStats.getTotal());
        result.setPerformance(duelStats.getPerformance());
        result.setSkillPoints(duelStats.getSkillPoints());
        if (action.equals(Action.POSITION)) {
            result.setTeamAssistance(duelStats.getTeamAssistance());
        }

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
            res.getModifiers().setHorizontalPressure(
                com.kjeldsen.match.rest.model.HorizontalPressure.valueOf(matchTeam.getHorizontalPressure().name()));
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
        result.setPosition(com.kjeldsen.match.rest.model.PlayerPosition.valueOf(player.getPosition().name()));
        return result;
    }

    private Team buildTeam(com.kjeldsen.player.domain.Team home, TeamRole role, Modifiers modifiers) {
        List<Player> activePlayers = playerRepo.findByTeamId(home.getId()).stream()
            .filter(player -> player.getStatus() == PlayerStatus.ACTIVE)
            .map(player -> this.buildPlayer(player, role))
            .toList();

        List<Player> benchPlayers = playerRepo.findByTeamId(home.getId()).stream()
            .filter(player -> player.getStatus() == PlayerStatus.BENCH)
            .map(player -> this.buildPlayer(player, role))
            .toList();

        return Team.builder()
            .id(home.getId().value())
            .role(role)
            .players(activePlayers)
            .bench(benchPlayers)
            .tactic(Tactic.valueOf(modifiers.getTactic().getValue()))
            .verticalPressure(VerticalPressure.valueOf(modifiers.getVerticalPressure().getValue()))
            .horizontalPressure(
                HorizontalPressure.valueOf(modifiers.getHorizontalPressure().getValue()))
            .build();
    }

    private Player buildPlayer(com.kjeldsen.player.domain.Player player, TeamRole teamRole) {
        Map<PlayerSkill, Integer> skills = player.getActualSkills().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getActual()));
        skills.put(PlayerSkill.INTERCEPTING, 0);

        return Player.builder()
            .id(player.getId().value())
            .name(player.getName())
            .status(player.getStatus())
            .teamId(player.getTeamId().value())
            .teamRole(teamRole)
            .position(player.getPosition())
            .skills(skills)
            .playerOrder(player.getPlayerOrder())
            .build();
    }
}
