package com.kjeldsen.player.test.mapper;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.rest.mapper.PlayerTrainingResponseMapper;
import com.kjeldsen.player.rest.model.PlayerTrainingResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class PlayerTrainingResponseMapperTest {

    @Test
    void should_map_from_playerTrainingEvent() {

        Player.PlayerId playerId = Player.PlayerId.generate();

        PlayerTrainingBloomEvent playerTrainingBloomEvent = PlayerTrainingBloomEvent.builder()
            .playerId(playerId)
            .bloomSpeed(5)
            .bloomStartAge(20)
            .yearsOn(3)
            .id(EventId.generate())
            .occurredAt(Instant.now())
            .build();

        PlayerTrainingEvent playerTrainingEvent = PlayerTrainingEvent.builder()
            .playerId(playerId)
            .pointsBeforeTraining(28)
            .pointsAfterTraining(30)
            .currentDay(1)
            .points(2)
            .skill(PlayerSkill.BALL_CONTROL)
            .bloom(playerTrainingBloomEvent)
            .id(EventId.generate())
            .occurredAt(Instant.now())
            .build();

        PlayerTrainingResponse playerTrainingResponse = PlayerTrainingResponseMapper.INSTANCE.fromPlayerTrainingEvent(playerTrainingEvent);

        Assertions.assertThat(playerTrainingResponse.getCurrentDay()).isEqualTo(playerTrainingEvent.getCurrentDay());
        Assertions.assertThat(playerTrainingResponse.getPointsAfterTraining()).isEqualTo(playerTrainingEvent.getPointsAfterTraining());
        Assertions.assertThat(playerTrainingResponse.getPointsBeforeTraining()).isEqualTo(playerTrainingEvent.getPointsBeforeTraining());
        Assertions.assertThat(playerTrainingResponse.getPoints()).isEqualTo(playerTrainingEvent.getPoints());

        Assertions.assertThat(playerTrainingResponse.getPlayerId()).isEqualTo(playerTrainingEvent.getPlayerId().value());
        Assertions.assertThat(playerTrainingResponse.getSkill()).isEqualTo(com.kjeldsen.player.rest.model.PlayerSkill.BALL_CONTROL);
    }

    @Test
    void should_map_playerSkill_from_playerSkillDomain() {
        com.kjeldsen.player.rest.model.PlayerSkill playerSkill = PlayerTrainingResponseMapper.INSTANCE.fromPlayerSkillDomain(PlayerSkill.BALL_CONTROL);
        Assertions.assertThat(playerSkill).isEqualTo(com.kjeldsen.player.rest.model.PlayerSkill.BALL_CONTROL);
    }
}
