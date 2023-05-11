package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.rest.model.PlayerPositionTendencyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerPositionTendencyMapper {

    PlayerPositionTendencyMapper INSTANCE = Mappers.getMapper(PlayerPositionTendencyMapper.class);


    PlayerPositionTendencyResponse map(PlayerPositionTendency playerPositionTendency);

    default String map(Player.PlayerId playerId) {
        return playerId.value();
    }

}
