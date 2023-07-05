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

    PlayerResponse map(Player player);

    default PlayerPosition map(com.kjeldsen.player.rest.model.PlayerPosition position) {
        return PlayerPosition.valueOf(position.name());
    }

    //TODO testing de este mapper por el default, no se testea en IT
    default PlayerSkill map(String playerSkill) {
        return PlayerSkill.valueOf(playerSkill);
    }

}
