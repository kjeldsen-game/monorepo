package com.kjeldsen.player.test.mapper;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.rest.mapper.PlayerMapper;
import com.kjeldsen.player.rest.model.PlayerResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerMapperTest {


    @Test
    void mapPlayerToPlayerResponse() {

        Player.PlayerId playerId = Player.PlayerId.generate();
        Integer playerAge = PlayerProvider.age();
        String playerName = PlayerProvider.name();

        Player player = Player.builder()
            .id(playerId)
            .name(playerName)
            .age(playerAge)
            .position(PlayerPosition.FORWARD)
            .actualSkills(Map.of(
                PlayerSkill.SCORE, new PlayerSkills(10, 0),
                PlayerSkill.PASSING, new PlayerSkills(20, 0)
            ))
            .build();

        PlayerResponse playerResponse = PlayerMapper.INSTANCE.playerResponseMap(player);

        assertEquals(playerId.value(), playerResponse.getId());
        assertEquals("10", playerResponse.getActualSkills().get("SCORE"));
        assertEquals(player.getName(), playerResponse.getName());
        assertEquals(player.getAge(), playerResponse.getAge());
        assertEquals(PlayerPosition.FORWARD.toString(), playerResponse.getPosition().toString());
        assertEquals(2, playerResponse.getActualSkills().size());
    }

    @Test
    void should_throw_an_exception_when_input_is_not_found_with_playerSkills() {
        String playerSkill = "PLAYER_HAPPINESS";
        assertThrows(IllegalArgumentException.class, () -> {
            PlayerMapper.INSTANCE.playerSkillMap(playerSkill);
        });
    }

    @Test
    void should_find_input_with_playerSkills() {
        String playerSkill = "BALL_CONTROL";
        com.kjeldsen.player.rest.model.PlayerSkill foundedSkill = PlayerMapper.INSTANCE.playerSkillMap(playerSkill);
        Assert.assertEquals(com.kjeldsen.player.rest.model.PlayerSkill.BALL_CONTROL, foundedSkill);
    }


    // TODO update this tests to test only default methods of the mapper interface. Do for all mappers.

}
