package com.kjeldsen.player.domain.repositories.queries;

import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
public class FilterMarketPlayersQuery {
    private PlayerPosition preferredPosition;
    private Integer minAge;
    private Integer maxAge;
    private List<PlayerSkillFilter> skills;
    private List<String> playerIds;


    @Getter
    @Builder
    @Setter
    @ToString
    public static class PlayerSkillFilter {
        private PlayerSkill playerSkill;

        @Builder.Default
        private Integer minValue = 0;
        @Builder.Default
        private Integer maxValue = 100;
        @Builder.Default
        private Integer minPotentialValue = 0;
        @Builder.Default
        private Integer maxPotentialValue = 100;
    }
}
