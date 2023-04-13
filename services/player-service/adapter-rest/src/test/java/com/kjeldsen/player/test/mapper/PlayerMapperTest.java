package com.kjeldsen.player.test.mapper;

import com.kjeldsen.player.domain.*;
import com.kjeldsen.player.rest.mapper.PlayerMapper;
import com.kjeldsen.player.rest.model.PlayerResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerMapperTest {

    @Test
    void mapPlayerToPlayerResponse() {

        PlayerId playerId = PlayerId.generate();
        PlayerAge playerAge = PlayerAge.of(20);
        PlayerName playerName = PlayerName.generate();

        Player player = Player.builder()
            .id(playerId)
            .name(playerName)
            .age(playerAge)
            .position(PlayerPosition.FORWARD)
            .actualSkills(PlayerActualSkills.of(
                Map.of(
                    PlayerSkill.SCORE, 10,
                    PlayerSkill.PASSING, 20
                )
            ))
            .build();

        PlayerResponse playerResponse = PlayerMapper.INSTANCE.map(player);

        assertEquals(playerId.toString(), playerResponse.getId());
        assertEquals("10", playerResponse.getActualSkills().get("SCORE"));
        assertEquals(playerName.value(), playerResponse.getName());
        assertEquals(playerAge.value(), playerResponse.getAge());
        assertEquals(PlayerPosition.FORWARD.toString(), playerResponse.getPosition().toString());
        assertEquals(2, playerResponse.getActualSkills().size());
    }

}
