package com.kjeldsen.match.domain.entities.stats;

import com.kjeldsen.match.common.RandomHelper;
import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.entities.TeamRole;
import com.kjeldsen.match.domain.entities.duel.DuelResult;
import com.kjeldsen.match.domain.entities.duel.DuelRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MatchStatsTest {

    static Team testTeam;
    static List<Player> players;

    @BeforeAll
    static void setUp() {
        testTeam = RandomHelper.genTeam(TeamRole.HOME);
        players = RandomHelper.genActivePlayers(testTeam);
    }

    @Test
    @DisplayName("Should init the Match stats")
    public void should_init_the_Match_stats() {
        MatchStats result = MatchStats.init(players);

        assertEquals(players.size(), result.getPlayersStats().size());
        assertThat(result.getPlayersStats().keySet()).containsAll(players.stream().map(Player::getId).collect(Collectors.toSet()));

        assertEquals(0, result.getGoals());
        assertEquals(0, result.getShots());
        assertEquals(0, result.getMissed());
        assertEquals(0, result.getSaved());
        assertEquals(0, result.getTackles());
        assertEquals(0, result.getFailedPasses());
        assertEquals(0, result.getPasses());
    }

    @Test
    @DisplayName("Should update the Pass stats")
    public void should_update_the_pass_stats() {
        MatchStats matchStats = MatchStats.init(players);
        matchStats.handlePassStats(DuelRole.INITIATOR, DuelResult.WIN, players.get(0).getId());

        assertThat(matchStats.getPasses()).isEqualTo(1);
        assertThat(matchStats.getPlayersStats().get(players.get(0).getId()).getPasses()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should update the Tackle stats")
    public void should_update_the_tackle_stats() {
        MatchStats matchStats = MatchStats.init(players);
        matchStats.handleTackleStats(DuelRole.CHALLENGER, DuelResult.LOSE, players.get(0).getId());

        assertThat(matchStats.getTackles()).isEqualTo(1);
        assertThat(matchStats.getPlayersStats().get(players.get(0).getId()).getTackles()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should update missed shot and total shots")
    public void should_update_missed_shot_stats() {
        MatchStats matchStats = MatchStats.init(players);
        matchStats.handleGoalStats(DuelRole.INITIATOR, DuelResult.LOSE, players.get(0).getId(), true);

        assertThat(matchStats.getShots()).isEqualTo(1);
        assertThat(matchStats.getPlayersStats().get(players.get(0).getId()).getShots()).isEqualTo(1);

        assertThat(matchStats.getMissed()).isEqualTo(1);
        assertThat(matchStats.getPlayersStats().get(players.get(0).getId()).getMissed()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should update goal shot and total shots")
    public void should_update_goal_shot_stats() {
        MatchStats matchStats = MatchStats.init(players);
        matchStats.handleGoalStats(DuelRole.INITIATOR, DuelResult.WIN, players.get(0).getId(), false);

        assertThat(matchStats.getShots()).isEqualTo(1);
        assertThat(matchStats.getPlayersStats().get(players.get(0).getId()).getShots()).isEqualTo(1);

        assertThat(matchStats.getGoals()).isEqualTo(1);
        assertThat(matchStats.getPlayersStats().get(players.get(0).getId()).getGoals()).isEqualTo(1);
    }

}