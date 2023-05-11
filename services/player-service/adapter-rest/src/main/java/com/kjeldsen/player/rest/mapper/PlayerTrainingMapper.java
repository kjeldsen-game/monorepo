package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.rest.model.PlayerTrainingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerTrainingMapper {

    PlayerTrainingMapper INSTANCE = Mappers.getMapper(PlayerTrainingMapper.class);

    PlayerTrainingEvent map(PlayerTrainingResponse playerTrainingResponse);

    default String map(Player.PlayerId playerId) {
        return playerId.value();
    }
}
