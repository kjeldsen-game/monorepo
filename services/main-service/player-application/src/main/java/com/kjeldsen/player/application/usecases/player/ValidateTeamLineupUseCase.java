package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerStatus;
import com.kjeldsen.player.domain.Team;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidateTeamLineupUseCase {

    static final String BENCH_PLAYERS_NUMBER = "7 bench players are required";
    static final String ACTIVE_PLAYERS_NUMBER = "11 active players are required";
    static final String GOALKEEPER_REQUIRED = "A goalkeeper is required";
    static final String MINIMUM_1_LB_LWB_IS_REQUIRED = "Back left flank coverage is required (LB or LWB)";
    static final String MAXIMUM_1_LB_LWB_IS_ALLOWED = "Only one player covering back left flank is allowed (LB or LWB)";
    static final String MINIMUM_1_RB_RWB_IS_REQUIRED = "Back right flank coverage is required (RB or RWB)";
    static final String MAXIMUM_1_RB_RWB_IS_ALLOWED = "Only one player covering back right flank is allowed (RB or RWB)";
    static final String MINIMUM_1_LM_LW_IS_REQUIRED = "Left flank coverage is required (LM or LW)";
    static final String MAXIMUM_1_LM_LW_IS_ALLOWED = "Only one player covering midfield left flank is allowed (LM or LW)";
    static final String MINIMUM_1_RM_RW_IS_REQUIRED = "Right flank coverage is required (RM or RW)";
    static final String MAXIMUM_1_RM_RW_ALLOWED = "Only one player covering right midfield flank is allowed (RM or RW)";
    static final String MINIMUM_1_FORWARD_IS_REQUIRED = "A minimum of 1 forward is required";
    static final String MAXIMUM_3_FORWARDS_ARE_ALLOWED = "A maximum of 3 forwards are allowed";
    static final String MAXIMUM_1_STRIKER_IS_ALLOWED = "Only 1 striker is allowed";
    static final String MINIMUM_3_MIDFIELDERS_ARE_REQUIRED = "A minimum of 3 midfielders are required";
    static final String MAXIMUM_6_MIDFIELDERS_ARE_ALLOWED = "A maximum of 6 midfielders are allowed";
    static final String MINIMUM_1_CENTRAL_MIDFIELDER_IS_REQUIRED = "At least one central midfielder is required";
    static final String MAXIMUM_1_DEFENSIVE_MIDFIELDER_IS_ALLOWED = "Only one defensive midfielder is allowed";
    static final String MAXIMUM_1_OFFENSIVE_MIDFIELDER_IS_ALLOWED = "Only one offensive midfielder is allowed";
    static final String MINIMUM_3_DEFENDERS_REQUIRED = "A minimum of 3 defenders are required";
    static final String MAXIMUM_5_DEFENDERS_ALLOWED = "A maximum of 5 defenders are allowed";
    static final String MAXIMUM_3_CENTRAL_DEFENDERS_ALLOWED = "A maximum of 3 central defenders are allowed";
    static final String MAXIMUM_1_SWEEPER_IS_ALLOWED = "Only one sweeper is allowed";

    public TeamFormationValidationResult validate(List<Player> players) {
        TeamFormationValidationResult result = new TeamFormationValidationResult();

        result.setValid(true);

        if (!players.isEmpty()) {
            List<Player> activePlayers = players.stream().filter(p -> p.getStatus() == PlayerStatus.ACTIVE).toList();
            List<Player> benchPlayers = players.stream().filter(p -> p.getStatus() == PlayerStatus.BENCH).toList();

            if (activePlayers.size() == 11) {
                result.addInfo(ACTIVE_PLAYERS_NUMBER);
            } else {
                result.addError(ACTIVE_PLAYERS_NUMBER);
            }

            if (benchPlayers.size() == 7) {
                result.addInfo(BENCH_PLAYERS_NUMBER);
            } else {
                result.addError(BENCH_PLAYERS_NUMBER);
            }

            if (activePlayers.stream()
                .filter(p -> PlayerPosition.GOALKEEPER.equals(p.getPosition()))
                .toList().size() == 1) {
                result.addInfo(GOALKEEPER_REQUIRED);
            } else {
                result.addError(GOALKEEPER_REQUIRED);
            }

            validateDefenders(activePlayers, result);

            validateMidfielders(activePlayers, result);

            validateAttackers(activePlayers, result);

            validateBackFlankCoverage(activePlayers, result);

            validateMidfieldFlankCoverage(activePlayers, result);

        }

        return result;
    }

    private static void validateBackFlankCoverage(List<Player> activePlayers, TeamFormationValidationResult result) {

        boolean leftDefender = activePlayers.stream().anyMatch(p -> PlayerPosition.LEFT_BACK.equals(p.getPosition()));
        boolean leftWingBack = activePlayers.stream().anyMatch(p -> PlayerPosition.LEFT_WINGBACK.equals(p.getPosition()));

        if (!leftDefender && !leftWingBack) {
            result.addError(MINIMUM_1_LB_LWB_IS_REQUIRED);
        } else {
            result.addInfo(MINIMUM_1_LB_LWB_IS_REQUIRED);
        }

        if (leftDefender && leftWingBack) {
            result.addError(MAXIMUM_1_LB_LWB_IS_ALLOWED);
        } else {
            result.addInfo(MAXIMUM_1_LB_LWB_IS_ALLOWED);
        }

        boolean rightDefender = activePlayers.stream().anyMatch(p -> PlayerPosition.RIGHT_BACK.equals(p.getPosition()));
        boolean rightWingback = activePlayers.stream().anyMatch(p -> PlayerPosition.RIGHT_WINGBACK.equals(p.getPosition()));

        if (!rightDefender && !rightWingback) {
            result.addError(MINIMUM_1_RB_RWB_IS_REQUIRED);
        } else {
            result.addInfo(MINIMUM_1_RB_RWB_IS_REQUIRED);
        }

        if (rightDefender && rightWingback) {
            result.addError(MAXIMUM_1_RB_RWB_IS_ALLOWED);
        } else {
            result.addInfo(MAXIMUM_1_RB_RWB_IS_ALLOWED);
        }
    }

    private static void validateMidfieldFlankCoverage(List<Player> activePlayers, TeamFormationValidationResult result) {
        boolean leftMidfielder = activePlayers.stream().anyMatch(p -> PlayerPosition.LEFT_MIDFIELDER.equals(p.getPosition()));
        boolean leftWinger = activePlayers.stream().anyMatch(p -> PlayerPosition.LEFT_WINGER.equals(p.getPosition()));

        if (!leftMidfielder && !leftWinger) {
            result.addError(MINIMUM_1_LM_LW_IS_REQUIRED);
        } else {
            result.addInfo(MINIMUM_1_LM_LW_IS_REQUIRED);
        }

        if (leftMidfielder && leftWinger) {
            result.addError(MAXIMUM_1_LM_LW_IS_ALLOWED);
        } else {
            result.addInfo(MAXIMUM_1_LM_LW_IS_ALLOWED);
        }

        boolean rightMidfielder = activePlayers.stream().anyMatch(p -> PlayerPosition.RIGHT_MIDFIELDER.equals(p.getPosition()));
        boolean rightWinger = activePlayers.stream().anyMatch(p -> PlayerPosition.RIGHT_WINGER.equals(p.getPosition()));

        if (!rightMidfielder && !rightWinger) {
            result.addError(MINIMUM_1_RM_RW_IS_REQUIRED);
        } else {
            result.addInfo(MINIMUM_1_RM_RW_IS_REQUIRED);
        }

        if (rightMidfielder && rightWinger) {
            result.addError(MAXIMUM_1_RM_RW_ALLOWED);
        } else {
            result.addInfo(MAXIMUM_1_RM_RW_ALLOWED);
        }
    }

    private static void validateAttackers(List<Player> activePlayers, TeamFormationValidationResult result) {
        List<Player> forwards = activePlayers.stream()
            .filter(p -> PlayerPosition.FORWARD.equals(p.getPosition())
                || PlayerPosition.AERIAL_FORWARD.equals(p.getPosition()))
            .toList();

        if (forwards.isEmpty()) {
            result.addError(MINIMUM_1_FORWARD_IS_REQUIRED);
        } else {
            result.addInfo(MINIMUM_1_FORWARD_IS_REQUIRED);
        }

        if (forwards.size() > 3) {
            result.addError(MAXIMUM_3_FORWARDS_ARE_ALLOWED);
        } else {
            result.addInfo(MAXIMUM_3_FORWARDS_ARE_ALLOWED);
        }

        List<Player> strikers = activePlayers.stream()
            .filter(p -> PlayerPosition.STRIKER.equals(p.getPosition())
                || PlayerPosition.AERIAL_STRIKER.equals(p.getPosition()))
            .toList();

        if (strikers.size() > 1) {
            result.addError(MAXIMUM_1_STRIKER_IS_ALLOWED);
        } else {
            result.addInfo(MAXIMUM_1_STRIKER_IS_ALLOWED);
        }
    }

    private static void validateMidfielders(List<Player> activePlayers, TeamFormationValidationResult result) {
        List<Player> midfielders = activePlayers.stream()
            .filter(p -> p.getPosition().isMidfielder())
            .toList();

        if (midfielders.size() < 3) {
            result.addError(MINIMUM_3_MIDFIELDERS_ARE_REQUIRED);
        } else {
            result.addInfo(MINIMUM_3_MIDFIELDERS_ARE_REQUIRED);
        }

        if (midfielders.size() > 6) {
            result.addError(MAXIMUM_6_MIDFIELDERS_ARE_ALLOWED);
        } else {
            result.addInfo(MAXIMUM_6_MIDFIELDERS_ARE_ALLOWED);
        }

        if (midfielders.stream().noneMatch(p -> p.getPosition().isCentral())) {
            result.addError(MINIMUM_1_CENTRAL_MIDFIELDER_IS_REQUIRED);
        } else {
            result.addInfo(MINIMUM_1_CENTRAL_MIDFIELDER_IS_REQUIRED);
        }

        List<Player> defensiveMidfielders = midfielders.stream()
            .filter(
                p -> PlayerPosition.DEFENSIVE_MIDFIELDER.equals(p.getPosition()))
            .toList();
        if (defensiveMidfielders.size() > 1) {
            result.addError(MAXIMUM_1_DEFENSIVE_MIDFIELDER_IS_ALLOWED);
        } else {
            result.addInfo(MAXIMUM_1_DEFENSIVE_MIDFIELDER_IS_ALLOWED);
        }

        List<Player> offensiveMidfielders = midfielders.stream()
            .filter(
                p -> PlayerPosition.OFFENSIVE_MIDFIELDER.equals(p.getPosition()))
            .toList();
        if (offensiveMidfielders.size() > 1) {
            result.addError(MAXIMUM_1_OFFENSIVE_MIDFIELDER_IS_ALLOWED);
        } else {
            result.addInfo(MAXIMUM_1_OFFENSIVE_MIDFIELDER_IS_ALLOWED);
        }
    }

    private static void validateDefenders(List<Player> activePlayers, TeamFormationValidationResult result) {
        List<Player> defenders = activePlayers.stream()
            .filter(p -> p.getPosition().isDefender()
                || p.getPosition().isWingback())
            .toList();

        if (defenders.size() < 3) {
            result.addError(MINIMUM_3_DEFENDERS_REQUIRED);
        } else {
            result.addInfo(MINIMUM_3_DEFENDERS_REQUIRED);
        }

        if (defenders.size() > 5) {
            result.addError(MAXIMUM_5_DEFENDERS_ALLOWED);
        } else {
            result.addInfo(MAXIMUM_5_DEFENDERS_ALLOWED);
        }

        List<Player> centralDefenders = defenders.stream()
            .filter(defender -> defender.getPosition().isCentral())
            .toList();

        if (centralDefenders.size() > 3) {
            result.addError(MAXIMUM_3_CENTRAL_DEFENDERS_ALLOWED);
        } else {
            result.addInfo(MAXIMUM_3_CENTRAL_DEFENDERS_ALLOWED);
        }

        List<Player> sweepers = activePlayers.stream()
            .filter(p -> PlayerPosition.SWEEPER.equals(p.getPosition()))
            .toList();

        if (sweepers.size() > 1) {
            result.addError(MAXIMUM_1_SWEEPER_IS_ALLOWED);
        } else {
            result.addInfo(MAXIMUM_1_SWEEPER_IS_ALLOWED);
        }

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class TeamFormationValidationResult {
        private Boolean valid;
        private List<TeamFormationValidationItem> items = new ArrayList<>();

        public void addInfo(String info) {
            this.items.add(new TeamFormationValidationItem(true, info));
        }

        public void addError(String error) {
            this.valid = false;
            this.items.add(new TeamFormationValidationItem(false, error));
        }
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class TeamFormationValidationItem {
        private Boolean valid;
        private String message;
    }
}
