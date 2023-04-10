package com.kjeldsen.player.test.mapper;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerActualSkills;
import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerName;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
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

        assertEquals(playerId.toString(), playerResponse.getId().toString());
        assertEquals("10", playerResponse.getActualSkills().get("SCORE"));
        // TODO add remaining testing
    }

}
