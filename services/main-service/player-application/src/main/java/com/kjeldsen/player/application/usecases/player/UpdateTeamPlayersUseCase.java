package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateTeamPlayersUseCase {

    private final TeamReadRepository teamReadRepository;
    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;

    public Team update(List<PlayerEdit> playerEdits, String teamId) {

        log.info("UpdateTeamPlayersUseCase for team {} for {} players", teamId, playerEdits.size());

        List<PlayerEdit> activePlayers = playerEdits.stream()
            .filter(playerEdit -> playerEdit.status().equals(PlayerStatus.ACTIVE))
            .toList();
        log.info("{}",activePlayers.size());
        List<PlayerEdit> benchPlayers = playerEdits.stream()
            .filter(playerEdit -> playerEdit.status().equals(PlayerStatus.BENCH
            ))
            .toList();
        log.info("{}",benchPlayers.size());

        if (playerEdits.stream()
            .filter(playerEdit -> playerEdit.status().equals(PlayerStatus.BENCH))
            .toList().size() != 7) {
            throw new RuntimeException("7 bench players are required!");
        }

        if (activePlayers.size() != 11) {
            throw new RuntimeException("11 active players are required!");
        }
        validateTeamFormation(activePlayers);

        List<Player> teamPlayers = playerReadRepository.findByTeamId(Team.TeamId.of(teamId));
        playerEdits.forEach(update -> {
            Player player = teamPlayers.stream().filter(p ->
                Objects.equals(p.getId().value(), update.id())).findAny().orElseThrow();

            // TODO make a specific use case for all player position changes
            boolean playerChangingPositionFromGoalkeeper = PlayerPosition.GOALKEEPER.equals(player.getPosition()) && !PlayerPosition.GOALKEEPER.equals(PlayerPosition.valueOf(update.position().name()));
            boolean playerChangingPositionToGoalkeeper = !PlayerPosition.GOALKEEPER.equals(player.getPosition()) && PlayerPosition.GOALKEEPER.equals(PlayerPosition.valueOf(update.position().name()));
            if (playerChangingPositionFromGoalkeeper || playerChangingPositionToGoalkeeper) throw new RuntimeException(player.getName() + ": GOALKEEPER position is not changeable.");

            player.setPlayerOrder(PlayerOrder.valueOf(update.playerOrder().name()));
            player.setPosition(PlayerPosition.valueOf(update.position().name()));
            player.setStatus(PlayerStatus.valueOf(update.status().name()));
            playerWriteRepository.save(player);
        });

        return teamReadRepository.findOneById(Team.TeamId.of(teamId)).orElseThrow();
    }

    private void validateTeamFormation(List<PlayerEdit> activePlayers) {
        List<PlayerEdit> goalkeepers = activePlayers.stream()
            .filter(playerUpdate -> extractPosition(playerUpdate) == PlayerPosition.GOALKEEPER)
            .toList();

        if (goalkeepers.size() != 1) {
            throw new RuntimeException("A goalkeeper is required");
        }

        validateDefenders(activePlayers);
        validateMidfielders(activePlayers);
        validateAttackers(activePlayers);
        validateBackFlankCoverage(activePlayers);
        validateMidfieldFlankCoverage(activePlayers);
    }

    private void validateBackFlankCoverage(List<PlayerEdit> activePlayers) {
        boolean leftDefender = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.LEFT_BACK::equals);
        boolean leftWingBack = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.LEFT_WINGBACK::equals);
        if (!leftDefender && !leftWingBack) {
            throw new RuntimeException("Back left flank coverage is required (LB or LWB)");
        }
        if (leftDefender && leftWingBack) {
            throw new RuntimeException(
                "Only one player covering back left flank is allowed (LB or LWB)");
        }

        boolean rightDefender = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.RIGHT_BACK::equals);
        boolean rightWingback = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.RIGHT_WINGBACK::equals);
        if (!rightDefender && !rightWingback) {
            throw new RuntimeException("Back right flank coverage is required (RB or RWB)");
        }
        if (rightDefender && rightWingback) {
            throw new RuntimeException(
                "Only one player covering back right flank is allowed (RB or RWB)");
        }
    }

    private void validateMidfieldFlankCoverage(List<PlayerEdit> activePlayers) {
        boolean leftMidfielder = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.LEFT_MIDFIELDER::equals);
        boolean leftWinger = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.LEFT_WINGER::equals);
        if (!leftMidfielder && !leftWinger) {
            throw new RuntimeException("Left flank coverage is required (LM or LW)");
        }
        if (leftMidfielder && leftWinger) {
            throw new RuntimeException(
                "Only one player covering midfield left flank is allowed (LM or LW)");
        }

        boolean rightMidfielder = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.RIGHT_MIDFIELDER::equals);
        boolean rightWinger = activePlayers.stream()
            .map(this::extractPosition)
            .anyMatch(PlayerPosition.RIGHT_WINGER::equals);
        if (!rightMidfielder && !rightWinger) {
            throw new RuntimeException("Right flank coverage is required (RM or RW)");
        }
        if (rightMidfielder && rightWinger) {
            throw new RuntimeException(
                "Only one player covering right midfield flank is allowed (RM or RW)");
        }
    }

    private void validateAttackers(List<PlayerEdit> activePlayers) {
        List<PlayerEdit> forwards = activePlayers.stream()
            .filter(playerUpdate -> extractPosition(playerUpdate) == PlayerPosition.FORWARD
                || extractPosition(playerUpdate) == PlayerPosition.AERIAL_FORWARD)
            .toList();

        if (forwards.isEmpty()) {
            throw new RuntimeException("A minimum of 1 forward is required");
        }

        if (forwards.size() > 3) {
            throw new RuntimeException("A maximum of 3 forwards are allowed");
        }

        List<PlayerEdit> strikers = activePlayers.stream()
            .filter(playerUpdate -> extractPosition(playerUpdate) == PlayerPosition.STRIKER
                || extractPosition(playerUpdate) == PlayerPosition.AERIAL_STRIKER)
            .toList();

        if (strikers.size() > 1) {
            throw new RuntimeException("Only 1 striker is allowed");
        }
    }

    private void validateMidfielders(List<PlayerEdit> activePlayers) {
        List<PlayerEdit> midfielders = activePlayers.stream()
            .filter(playerUpdate -> extractPosition(playerUpdate).isMidfielder())
            .toList();

        if (midfielders.size() < 3) {
            throw new RuntimeException("A minimum of 3 midfielders are required");
        }

        if (midfielders.size() > 6) {
            throw new RuntimeException("A maximum of 6 midfielders are allowed");
        }

        if (midfielders.stream().noneMatch(midfielder -> extractPosition(midfielder).isCentral())) {
            throw new RuntimeException("At least one central midfielder is required");
        }

        List<PlayerEdit> defensiveMidfielders = midfielders.stream()
            .filter(
                midfielder -> extractPosition(midfielder) == PlayerPosition.DEFENSIVE_MIDFIELDER)
            .toList();
        if (defensiveMidfielders.size() > 1) {
            throw new RuntimeException("Only one defensive midfielder is allowed");
        }

        List<PlayerEdit> offensiveMidfielders = midfielders.stream()
            .filter(
                midfielder -> extractPosition(midfielder) == PlayerPosition.OFFENSIVE_MIDFIELDER)
            .toList();
        if (offensiveMidfielders.size() > 1) {
            throw new RuntimeException("Only one offensive midfielder is allowed");
        }
    }

    private void validateDefenders(List<PlayerEdit> activePlayers) {
        List<PlayerEdit> defenders = activePlayers.stream()
            .filter(playerUpdate -> extractPosition(playerUpdate).isDefender()
                || extractPosition(playerUpdate).isWingback())
            .toList();

        if (defenders.size() < 3) {
            throw new RuntimeException("A minimum of 3 defenders are required");
        }
        if (defenders.size() > 5) {
            throw new RuntimeException("A maximum of 5 defenders are allowed");
        }

        List<PlayerEdit> centralDefenders = defenders.stream()
            .filter(defender -> extractPosition(defender).isCentral())
            .toList();

        if (centralDefenders.size() > 3) {
            throw new RuntimeException("A maximum of 3 central defenders are allowed");
        }

        List<PlayerEdit> sweepers = activePlayers.stream()
            .filter(playerUpdate -> extractPosition(playerUpdate) == PlayerPosition.SWEEPER)
            .toList();

        if (sweepers.size() > 1) {
            throw new RuntimeException("Only one sweeper is allowed");
        }
    }

    private PlayerPosition extractPosition(PlayerEdit playerEdit) {
        return PlayerPosition.valueOf(playerEdit.position().name());
    }

    public record PlayerEdit(String id, PlayerStatus status, PlayerPosition position, PlayerOrder playerOrder) {
    }
}
