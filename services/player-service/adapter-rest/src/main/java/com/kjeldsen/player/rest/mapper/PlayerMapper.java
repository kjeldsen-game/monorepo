package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.rest.model.PlayerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerResponse map(Player player);

    default String map(Player.PlayerId playerId) {
        return playerId.value();
    }

}
