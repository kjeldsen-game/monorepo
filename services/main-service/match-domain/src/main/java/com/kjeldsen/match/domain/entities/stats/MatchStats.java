package com.kjeldsen.match.domain.entities.stats;

import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.duel.DuelResult;
import com.kjeldsen.match.domain.entities.duel.DuelRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "MatchStats")
@TypeAlias("MatchStats")
@EqualsAndHashCode(callSuper = true)
public class MatchStats extends Stats {

    Map<String, PlayerStats> playersStats = new HashMap<>();

    public MatchStats handleGoalStats(DuelRole role, DuelResult result, String playerId) {
        PlayerStats playerStats = this.playersStats.computeIfAbsent(playerId, id -> new PlayerStats());

        if (role.equals(DuelRole.CHALLENGER)) {
            // From perspective of attacker if he lost duel he did not score
            if (result.equals(DuelResult.LOSE)) {
                this.setSaved(this.getSaved() + 1);
                playerStats.setSaved(playerStats.getSaved() + 1);
            }
        } else {
            if (result.equals(DuelResult.LOSE)) {
                playerStats.setMissed(playerStats.getMissed() + 1);
                this.setMissed(this.getMissed() + 1);
            } else {
                playerStats.setScore(playerStats.getScore() + 1);
                this.setScore(this.getScore() + 1);
            }
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

    public MatchStats handlePassStats(DuelRole role, DuelResult result, String playerId) {
        PlayerStats playerStats = this.playersStats.get(playerId);
        // Right now the pass is always successful and cannot be failed
        if (role.equals(DuelRole.INITIATOR)) {
            if (result.equals(DuelResult.WIN)) {
                playerStats.setPasses(playerStats.getPasses() + 1);
                this.setPasses(this.getPasses() + 1);
            }
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
