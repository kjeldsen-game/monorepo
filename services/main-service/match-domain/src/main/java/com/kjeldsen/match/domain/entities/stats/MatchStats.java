package com.kjeldsen.match.domain.entities.stats;

import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.duel.DuelDisruption;
import com.kjeldsen.match.domain.entities.duel.DuelDisruptor;
import com.kjeldsen.match.domain.entities.duel.DuelResult;
import com.kjeldsen.match.domain.entities.duel.DuelRole;
import com.kjeldsen.player.domain.PitchArea;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@Document(collection = "MatchStats")
@TypeAlias("MatchStats")
@EqualsAndHashCode(callSuper = true)
public class MatchStats extends Stats {

    Map<String, PlayerStats> playersStats = new HashMap<>();

    public MatchStats handleGoalStats(DuelRole role, DuelResult result, String playerId, boolean missed) {
        PlayerStats playerStats = this.playersStats.computeIfAbsent(playerId, id -> new PlayerStats());

        if (role.equals(DuelRole.CHALLENGER)) {
            // From perspective of attacker if he lost duel he did not score
            if (result.equals(DuelResult.LOSE) && !missed) {
                this.setSaved(this.getSaved() + 1);
                playerStats.setSaved(playerStats.getSaved() + 1);
            }
        } else {
            if (result.equals(DuelResult.LOSE)) {
                if (missed) {
                    playerStats.setMissed(playerStats.getMissed() + 1);
                    this.setMissed(this.getMissed() + 1);
                }
            } else {
                playerStats.setGoals(playerStats.getGoals() + 1);
                this.setGoals(this.getGoals() + 1);
            }
            playerStats.setShots(playerStats.getShots() + 1);
            this.setShots(this.getShots() + 1);
        }
        return this;
    }

    public MatchStats handleTackleStats(DuelRole role, DuelResult result, String playerId) {
        PlayerStats playerStats = this.playersStats.get(playerId);
        if (role.equals(DuelRole.CHALLENGER)) {
            if (result.equals(DuelResult.LOSE)) {
                playerStats.setTackles(playerStats.getTackles() + 1);
                this.setTackles(this.getTackles() + 1);
            }
        }
        return this;
    }

    public MatchStats handlePassStats(DuelRole role, DuelResult result, String playerId, DuelDisruption disruption) {
        PlayerStats playerStats = this.playersStats.get(playerId);
        // Right now the pass is always successful and cannot be failed
        if (role.equals(DuelRole.INITIATOR)) {
            if (disruption != null && disruption.getType().equals(DuelDisruptor.MISSED_PASS)) {
                this.setMissedPasses(this.getMissedPasses() + 1);
                playerStats.setMissedPasses(playerStats.getMissedPasses() + 1);
            }
            playerStats.setPasses(playerStats.getPasses() + 1);
            this.setPasses(this.getPasses() + 1);
        }
        return this;
    }

    public static MatchStats init(List<Player> players) {
        MatchStats stats = new MatchStats();
        for (Player player : players) {
            stats.playersStats.put(player.getId(), new PlayerStats());
        }
        return stats;
    }
}
