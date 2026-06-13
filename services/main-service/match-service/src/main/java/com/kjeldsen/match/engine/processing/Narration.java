package com.kjeldsen.match.engine.processing;

import com.kjeldsen.match.engine.entities.Action;
import com.kjeldsen.match.engine.entities.Play;
import com.kjeldsen.match.engine.entities.duel.Duel;
import com.kjeldsen.match.engine.entities.duel.DuelResult;
import com.kjeldsen.match.engine.entities.duel.DuelType;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.models.DuelStats;
import com.kjeldsen.match.models.Player;
import java.util.List;
import java.util.stream.Collectors;

public class Narration {

    /*
     * Ignore - this will be moved to frontend instead of printing to the console
     */

    public static List<String> narrate(GameState endState) {
        return endState.getPlays().stream()
            .map(Narration::describePlay)
            .collect(Collectors.toList());
    }

    public static String describePlay(Play play) {
        String area = play.getAction() == Action.PASS && play.getDuel().getReceiverArea() != null
            ? String.format("%s → %s",
                play.getDuel().getPitchArea().displayName(),
                play.getDuel().getReceiverArea().displayName())
            : play.getDuel().getPitchArea().displayName();

        String meta = String.format("Clock: %d\nArea: %s\nAction: %s",
            play.getClock(), area, play.getAction());

        Player initiator = play.getDuel().getInitiator();
        String attempt = String.format("%s [%s] %s",
            initiator.getName(),
            initiator.getPosition(),
            describeInitiatorAction(play));

        if (play.getDuel().getReceiver() != null) {
            Player receiver = play.getDuel().getReceiver();
            attempt = attempt.concat(
                String.format(" to %s [%s]",
                    receiver.getName(), receiver.getPosition().toString()));
        }

        Player challenger = play.getDuel().getChallenger();
        String response = "";
        if (play.getAction() != Action.PASS) {
            response = challenger != null
                ? String.format("Duel challenger: %s [%s] %s",
                    challenger.getName(),
                    challenger.getPosition().toString(),
                    describeChallengerAction(play))
                : "Duel challenger: none";
        }

        String outcome =
            play.getAction() == Action.POSITION
                ? describePositionalOutcome(play, initiator, challenger)
                : describeOutcome(play, initiator, challenger);

        String stats = getDuelStats(play.getDuel());

        StringBuilder sb = new StringBuilder();
        sb.append(meta).append("\n");
        sb.append(attempt).append("\n");
        if (!response.isEmpty()) sb.append(response).append("\n");
        if (outcome != null) sb.append(outcome).append("\n");
        sb.append(stats).append("\n");
        return sb.toString();
    }

    private static String describePositionalOutcome(
        Play play, Player initiator, Player challenger) {

        String initiatorAssistance =
            describeAssistance(play.getDuel().getInitiatorStats().getAssistance() != null
                ? play.getDuel().getInitiatorStats().getAssistance() : 0);

        String initiatorOutcome =
            "%s had %s spacing help".formatted(initiator.getName(), initiatorAssistance);

        if (challenger == null) {
            int difference = play.getDuel().getInitiatorStats().getTotal()
                - play.getDuel().getChallengerStats().getTotal();
            String position = "%s was %s".formatted(initiator.getName(), describePosition(difference));
            return "%s\n%s".formatted(initiatorOutcome, position);
        }

        String challengerAssistance =
            describeAssistance(play.getDuel().getChallengerStats().getAssistance() != null
                ? play.getDuel().getChallengerStats().getAssistance() : 0);

        String challengerOutcome =
            "%s received %s support".formatted(challenger.getName(), challengerAssistance);

        int difference =
            play.getDuel().getInitiatorStats().getTotal()
                - play.getDuel().getChallengerStats().getTotal();
        String position = "%s was %s".formatted(
            play.getDuel().getChallenger().getName(), describePosition(difference));

        return "%s\n%s\n%s".formatted(initiatorOutcome, challengerOutcome, position);
    }

    public static String describeOutcome(Play play, Player initiator, Player challenger) {
        String initiatorName = initiator.getName();
        String challengerName = challenger != null ? challenger.getName() : "no challenger";
        if (play.getDuel().getResult() == DuelResult.WIN) {
            return switch (play.getAction()) {
                case PASS -> "The pass was successful";
                case POSITION -> null;
                case TACKLE -> "%s controlled the ball".formatted(initiatorName);
                case SHOOT -> "%s scored a goal".formatted(initiatorName);
            };
        }

        return switch (play.getAction()) {
            case PASS -> "%s intercepted the ball".formatted(challengerName);
            case POSITION -> null;
            case TACKLE -> "%s stole the ball".formatted(challengerName);
            case SHOOT -> "%s saved the ball".formatted(challengerName);
        };
    }

    public static String describeInitiatorAction(Play play) {
        Action action = play.getAction();

        String denomination =
            describePerformance(
                play.getDuel().getInitiatorStats().getPerformance()
                    + play.getDuel().getInitiatorStats().getSkillPoints());

        return switch (action) {
            case PASS -> "made %s pass".formatted(denomination);
            case TACKLE -> "attempted %s dribble".formatted(denomination);
            case SHOOT -> "made %s shot".formatted(denomination);
            case POSITION -> "tried with %s effort to get free".formatted(denomination);
        };
    }

    public static String describeChallengerAction(Play play) {
        Action action = play.getAction();

        String denomination =
            describePerformance(
                play.getDuel().getChallengerStats().getPerformance()
                    + play.getDuel().getChallengerStats().getSkillPoints());

        return switch (action) {
            case PASS -> "made %s attempt to intercept the ball".formatted(denomination);
            case TACKLE -> "made %s tackle attempt".formatted(denomination);
            case SHOOT -> "attempted %s save".formatted(denomination);
            case POSITION -> "made %s attempt to stay close".formatted(denomination);
        };
    }

    public static String getDuelStats(Duel duel) {
        String result = "%s Duel stats:\n".formatted(duel.getType())
            + " ───────────────── %s ──────────────────\n".formatted(duel.getInitiator().getName())
            + getPlayerStats(duel.getInitiatorStats());

        if (duel.getType() == DuelType.PASSING) {
            return result;
        }

        String challengerName = duel.getChallenger() != null
            ? duel.getChallenger().getName()
            : "no challenger";

        return result
            + " ───────────────── %s ──────────────────\n".formatted(challengerName)
            + getPlayerStats(duel.getChallengerStats());
    }

    public static String getPlayerStats(DuelStats duelStats) {
        String stats = "";
        if (duelStats.getSkillPoints() != null) {
            stats += "Skill contribution: " + duelStats.getSkillPoints() + "\n";
        }
        if (duelStats.getPerformance() != null) {
            stats += "Performance: " + duelStats.getPerformance() + "\n";
        }
        if (duelStats.getAssistance() != null) {
            stats += "Assistance: " + duelStats.getAssistance() + "\n";
        }
        if (duelStats.getCarryover() != null) {
            stats += "Carryover: " + duelStats.getCarryover() + "\n";
        }
        if (duelStats.getTotal() != null) {
            stats += "Total: " + duelStats.getTotal() + "\n";
        }
        return stats;
    }

    public static String describePosition(int positionalDuelPerformance) {
        if (positionalDuelPerformance <= -21) {
            return "very far";
        }
        if (positionalDuelPerformance <= -16) {
            return "far";
        }
        if (positionalDuelPerformance <= -11) {
            return "in the vicinity";
        }
        if (positionalDuelPerformance <= -6) {
            return "close";
        }
        if (positionalDuelPerformance <= -1) {
            return "almost there";
        }
        if (positionalDuelPerformance <= 0) {
            return "there";
        }
        if (positionalDuelPerformance <= 5) {
            return "just about";
        }
        if (positionalDuelPerformance <= 10) {
            return "near";
        }
        if (positionalDuelPerformance <= 15) {
            return "within reach";
        }
        if (positionalDuelPerformance <= 20) {
            return "ready";
        }
        return "in perfect position";
    }

    public static String describePerformance(int performance) {
        if (performance <= 15) {
            return "an awful";
        }
        if (performance <= 30) {
            return "a poor";
        }
        if (performance <= 40) {
            return "a weak";
        }
        if (performance <= 50) {
            return "a decent";
        }
        if (performance <= 60) {
            return "a good";
        }
        if (performance <= 70) {
            return "an excellent";
        }
        if (performance <= 80) {
            return "a superb";
        }
        if (performance <= 90) {
            return "a brilliant";
        }
        if (performance <= 100) {
            return "an awesome";
        }
        if (performance <= 110) {
            return "a masterful";
        }
        return "an unbelievable";
    }

    public static String describeAssistance(int assistance) {
        if (assistance <= -21) {
            return "negligible";
        }
        if (assistance <= -16) {
            return "minimal";
        }
        if (assistance <= -11) {
            return "inadequate";
        }
        if (assistance <= -6) {
            return "limited";
        }
        if (assistance <= -1) {
            return "restricted";
        }
        if (assistance <= 0) {
            return "basic";
        }
        if (assistance <= 5) {
            return "adequate";
        }
        if (assistance <= 10) {
            return "valuable";
        }
        if (assistance <= 15) {
            return "exceptional";
        }
        if (assistance <= 20) {
            return "outstanding";
        }
        return "extraordinary";
    }
}
