package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.rest.model.PlayerResponse;
import com.kjeldsen.player.rest.model.PlayerSkill;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {IdMapper.class})
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerResponse playerResponseMap(Player player);

    default PlayerPosition playerPositionMap(com.kjeldsen.player.rest.model.PlayerPosition position) {
        return PlayerPosition.valueOf(position.name());
    }

    default PlayerSkill playerSkillMap(String playerSkill) {
        return PlayerSkill.valueOf(playerSkill);
    }

    default com.kjeldsen.player.domain.PlayerSkill map(String playerSkill) {
        return com.kjeldsen.player.domain.PlayerSkill.valueOf(playerSkill);
    }

    default com.kjeldsen.player.rest.model.PlayerSkills map(com.kjeldsen.player.domain.PlayerSkills playerSkills) {
        com.kjeldsen.player.rest.model.PlayerSkills var1 = new com.kjeldsen.player.rest.model.PlayerSkills();
        var1.setActual(playerSkills.getActual());
        var1.setPotential(playerSkills.getPotential());
        return var1;
    }
}
