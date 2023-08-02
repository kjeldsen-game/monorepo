package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.Player;
import org.mapstruct.Mapper;

@Mapper
public interface IdMapper {

    default String map(Player.PlayerId playerId) {
        return playerId.value();
    }

}
