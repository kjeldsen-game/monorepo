package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.rest.model.PlayerDeclineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerDeclineMapper {
    PlayerDeclineMapper INSTANCE = Mappers.getMapper(PlayerDeclineMapper.class);

    PlayerDeclineResponse map(PlayerTrainingDeclineEvent playerTrainingDeclineEvent);

    String map(Player.PlayerId value);
}
