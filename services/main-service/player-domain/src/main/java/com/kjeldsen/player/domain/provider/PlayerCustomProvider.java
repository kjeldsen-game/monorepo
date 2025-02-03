package com.kjeldsen.player.domain.provider;

import com.kjeldsen.player.domain.PlayerPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class PlayerCustomProvider {

    private static final List<PlayerStats> playerStatsList = new ArrayList<>();

    static {
        // GK Stats
        playerStatsList.add(new PlayerStats(PlayerPosition.GOALKEEPER, new int[]{80, 60, 50, 50, 50, 50, 340}));
        playerStatsList.add(new PlayerStats(PlayerPosition.GOALKEEPER, new int[]{60, 80, 50, 50, 50, 50, 340}));
        playerStatsList.add(new PlayerStats(PlayerPosition.GOALKEEPER, new int[]{70, 70, 70, 70, 70, 70, 420}));
        playerStatsList.add(new PlayerStats(PlayerPosition.GOALKEEPER, new int[]{65, 65, 90, 90, 90, 90, 490}));

        // CB Stats
        playerStatsList.add(new PlayerStats(PlayerPosition.CENTRE_BACK, new int[]{20, 20, 20, 30, 60, 90, 60, 90, 390}));
        playerStatsList.add(new PlayerStats(PlayerPosition.CENTRE_BACK, new int[]{20, 20, 20, 30, 60, 90, 90, 60, 390}));
        playerStatsList.add(new PlayerStats(PlayerPosition.CENTRE_BACK, new int[]{20, 20, 20, 30, 60, 90, 75, 75, 390}));
        playerStatsList.add(new PlayerStats(PlayerPosition.CENTRE_BACK, new int[]{20, 20, 20, 30, 60, 90, 75, 75, 390}));
        playerStatsList.add(new PlayerStats(PlayerPosition.CENTRE_BACK, new int[]{20, 20, 20, 30, 70, 60, 85, 85, 390}));
        playerStatsList.add(new PlayerStats(PlayerPosition.CENTRE_BACK, new int[]{20, 20, 20, 30, 80, 90, 65, 65, 390}));
        playerStatsList.add(new PlayerStats(PlayerPosition.CENTRE_BACK, new int[]{20, 20, 20, 30, 100, 90, 60, 70, 410}));
        playerStatsList.add(new PlayerStats(PlayerPosition.CENTRE_BACK, new int[]{20, 20, 20, 30, 100, 90, 70, 60, 410}));
        playerStatsList.add(new PlayerStats(PlayerPosition.CENTRE_BACK, new int[]{20, 20, 30, 60, 60, 90, 65, 65, 410}));
        playerStatsList.add(new PlayerStats(PlayerPosition.CENTRE_BACK, new int[]{20, 20, 30, 60, 60, 90, 65, 65, 410}));

        // LB/LWB Stats
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_BACK, new int[]{20, 20, 40, 60, 45, 90, 70, 50, 395}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_WINGBACK, new int[]{20, 20, 40, 60, 45, 90, 50, 70, 395}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_BACK, new int[]{20, 20, 40, 60, 45, 90, 60, 60, 395}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_WINGBACK, new int[]{20, 20, 30, 40, 45, 90, 70, 70, 385}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_BACK, new int[]{20, 30, 55, 60, 45, 90, 60, 45, 405}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_WINGBACK, new int[]{20, 30, 55, 60, 45, 90, 45, 60, 405}));

        // RB/RWB Stats
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_BACK, new int[]{20, 20, 40, 60, 45, 90, 70, 50, 395}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_WINGBACK, new int[]{20, 20, 40, 60, 45, 90, 50, 70, 395}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_BACK, new int[]{20, 20, 40, 60, 45, 90, 60, 60, 395}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_WINGBACK, new int[]{20, 20, 30, 40, 45, 90, 70, 70, 385}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_BACK, new int[]{20, 30, 55, 60, 45, 90, 60, 45, 405}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_WINGBACK, new int[]{20, 30, 55, 60, 45, 90, 45, 60, 405}));

        // DM/CM Stats
// Updated code for adding DM/CM players
        playerStatsList.add(new PlayerStats(PlayerPosition.DEFENSIVE_MIDFIELDER, new int[]{20, 30, 50, 50, 50, 90, 60, 60, 410}));
        playerStatsList.add(new PlayerStats(PlayerPosition.CENTRE_MIDFIELDER, new int[]{20, 30, 60, 60, 50, 90, 50, 50, 410}));
        playerStatsList.add(new PlayerStats(PlayerPosition.DEFENSIVE_MIDFIELDER, new int[]{20, 30, 65, 65, 50, 90, 45, 45, 410}));
        playerStatsList.add(new PlayerStats(PlayerPosition.CENTRE_MIDFIELDER, new int[]{20, 30, 60, 70, 50, 90, 45, 45, 410}));
        playerStatsList.add(new PlayerStats(PlayerPosition.DEFENSIVE_MIDFIELDER, new int[]{20, 30, 70, 60, 50, 90, 45, 45, 410}));
        playerStatsList.add(new PlayerStats(PlayerPosition.CENTRE_MIDFIELDER, new int[]{20, 30, 50, 80, 50, 90, 45, 45, 410}));


// Updated code for adding LM/LW players
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_MIDFIELDER, new int[]{20, 40, 70, 70, 50, 90, 40, 40, 420}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_WINGER, new int[]{20, 40, 60, 80, 50, 90, 40, 40, 420}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_MIDFIELDER, new int[]{20, 40, 80, 60, 50, 90, 40, 40, 420}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_WINGER, new int[]{20, 50, 80, 80, 50, 90, 25, 25, 420}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_MIDFIELDER, new int[]{20, 50, 70, 90, 50, 90, 25, 25, 420}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_WINGER, new int[]{20, 50, 90, 70, 50, 90, 25, 25, 420}));
        playerStatsList.add(new PlayerStats(PlayerPosition.LEFT_MIDFIELDER, new int[]{40, 50, 70, 70, 50, 90, 25, 25, 420}));

        // RM/RW Stats
// RM/RW Stats
        playerStatsList.add(new PlayerStats(PlayerPosition.RIGHT_MIDFIELDER, new int[]{20, 40, 70, 70, 50, 90, 40, 40, 420}));
        playerStatsList.add(new PlayerStats(PlayerPosition.RIGHT_WINGER, new int[]{20, 40, 60, 80, 50, 90, 40, 40, 420}));
        playerStatsList.add(new PlayerStats(PlayerPosition.RIGHT_MIDFIELDER, new int[]{20, 40, 80, 60, 50, 90, 40, 40, 420}));
        playerStatsList.add(new PlayerStats(PlayerPosition.RIGHT_WINGER, new int[]{20, 50, 80, 80, 50, 90, 25, 25, 420}));
        playerStatsList.add(new PlayerStats(PlayerPosition.RIGHT_MIDFIELDER, new int[]{20, 50, 70, 90, 50, 90, 25, 25, 420}));
        playerStatsList.add(new PlayerStats(PlayerPosition.RIGHT_WINGER, new int[]{20, 50, 90, 70, 50, 90, 25, 25, 420}));

// FW Stats
        playerStatsList.add(new PlayerStats(PlayerPosition.FORWARD, new int[]{30, 60, 80, 70, 60, 90, 25, 25, 445}));
        playerStatsList.add(new PlayerStats(PlayerPosition.FORWARD, new int[]{30, 60, 90, 80, 60, 90, 25, 25, 445}));
        playerStatsList.add(new PlayerStats(PlayerPosition.FORWARD, new int[]{30, 60, 80, 90, 60, 90, 25, 25, 445}));
        playerStatsList.add(new PlayerStats(PlayerPosition.FORWARD, new int[]{40, 70, 90, 80, 70, 90, 25, 25, 445}));
        playerStatsList.add(new PlayerStats(PlayerPosition.FORWARD, new int[]{40, 70, 90, 90, 70, 90, 25, 25, 445}));
        playerStatsList.add(new PlayerStats(PlayerPosition.FORWARD, new int[]{50, 80, 90, 80, 80, 90, 25, 25, 445}));


    }

    public static List<PlayerStats> getPlayerStatsList() {
        return playerStatsList;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class PlayerStats {

        private PlayerPosition position;
        private int[] stats;
    }
}
