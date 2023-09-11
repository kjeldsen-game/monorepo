package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.rest.model.PlayerResponse;
import com.kjeldsen.player.rest.model.PlayerSkill;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper(uses = {IdMapper.class})
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerResponse playerPositionMap(Player player);

    default PlayerPosition playerPositionMap(com.kjeldsen.player.rest.model.PlayerPosition position) {
        return PlayerPosition.valueOf(position.name());
    }
    default PlayerSkill playerSkillMap(String playerSkill) {
        return PlayerSkill.valueOf(playerSkill);
    }


}
