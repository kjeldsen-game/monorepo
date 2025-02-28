package com.kjeldsen.player.test.mapper;

import com.kjeldsen.player.domain.*;
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
        PlayerAge playerAge = PlayerAge.generateAgeOfAPlayer();
        String playerName = PlayerProvider.name();

        Player player = Player.builder()
            .id(playerId)
            .teamId(Team.TeamId.of("teamId"))
            .name(playerName)
            .age(playerAge)
            .position(PlayerPosition.FORWARD)
            .actualSkills(Map.of(
                PlayerSkill.SCORING, new PlayerSkills(10, 0, PlayerSkillRelevance.CORE),
                PlayerSkill.PASSING, new PlayerSkills(20, 0, PlayerSkillRelevance.SECONDARY)
            ))
            .build();

        PlayerResponse playerResponse = PlayerMapper.INSTANCE.playerResponseMap(player);

        assertEquals(playerId.value(), playerResponse.getId());
        assertEquals(10, playerResponse.getActualSkills().get("SCORING").getPlayerSkills().getActual());
        assertEquals(player.getName(), playerResponse.getName());
        assertEquals(player.getAge().getYears(), playerResponse.getAge().getYears().intValue());
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
