package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;
import com.kjeldsen.player.rest.model.PlayerDeclineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {IdMapper.class})
public interface PlayerDeclineResponseMapper {
    PlayerDeclineResponseMapper INSTANCE = Mappers.getMapper(PlayerDeclineResponseMapper.class);

    PlayerDeclineResponse map(PlayerTrainingDeclineEvent playerTrainingDeclineEvent);
}
