package com.kjeldsen.player.test.mapper;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.provider.PlayerProvider;
import com.kjeldsen.player.rest.mapper.PlayerMapper;
import com.kjeldsen.player.rest.model.PlayerResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

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
                PlayerSkill.SCORE, 10,
                PlayerSkill.PASSING, 20
            ))
            .build();

        PlayerResponse playerResponse = PlayerMapper.INSTANCE.map(player);

        assertEquals(playerId.value(), playerResponse.getId());
        assertEquals("10", playerResponse.getActualSkills().get("SCORE"));
        assertEquals(player.getName(), playerResponse.getName());
        assertEquals(player.getAge(), playerResponse.getAge());
        assertEquals(PlayerPosition.FORWARD.toString(), playerResponse.getPosition().toString());
        assertEquals(2, playerResponse.getActualSkills().size());
    }

}
