package com.kjeldsen.player.domain.repositories.queries;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.Team;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class FilterMarketPlayersQuery {
    private PlayerPosition position;
    private Integer minAge;
    private Integer maxAge;
    private List<PlayerSkillFilter> skills;
    private List<Player.PlayerId> playerIds;


    @Getter
    @Builder
    public static class PlayerSkillFilter {
        PlayerSkill playerSkill;
        Integer minValue;
        Integer maxValue;
    }

}
