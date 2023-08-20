package com.kjeldsen.player.integration.player;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.Team;

import static com.kjeldsen.player.domain.provider.PlayerProvider.*;

public class PlayerProviderTestData {
    public static Player generate(Team.TeamId teamId, PlayerPositionTendency positionTendencies, int totalPoints) {
        return Player.builder()
            .id(Player.PlayerId.generate())
            .name(name())
            .age(age())
            .position(positionTendencies.getPosition())
            .actualSkills(skillsBasedOnTendency(positionTendencies, totalPoints))
            .teamId(teamId)
            .build();
    }

    public static Player generateDefault() {
        return generate(Team.TeamId.generate(), PlayerPositionTendency.DEFAULT_FORWARD_TENDENCIES, 200);
    }
}
