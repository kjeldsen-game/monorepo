package com.kjeldsen.player.test.mapper;

import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.rest.mapper.PlayerTrainingResponseMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlayerTrainingResponseMapperTest {

    @Test
    void should_map_playerSkill_from_playerSkillDomain() {
        com.kjeldsen.player.rest.model.PlayerSkill playerSkill = PlayerTrainingResponseMapper.INSTANCE.fromPlayerSkillDomain(PlayerSkill.BALL_CONTROL);
        Assertions.assertThat(playerSkill).isEqualTo(com.kjeldsen.player.rest.model.PlayerSkill.BALL_CONTROL);
    }
}
