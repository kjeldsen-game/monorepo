package com.kjeldsen.player.domain.schedulers;

import com.kjeldsen.player.domain.Player;

public interface PlayerTrainingScheduler {
    void scheduleTraining(Player.PlayerId playerId);
}
