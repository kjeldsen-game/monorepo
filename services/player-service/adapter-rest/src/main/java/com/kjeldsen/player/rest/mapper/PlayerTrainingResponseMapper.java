package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.rest.model.PlayerTrainingResponse;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PlayerTrainingResponseMapper {

    @Mapping(target = "bloom", ignore = true)
    PlayerTrainingEvent convertPlayerTrainingResponseToPlayerTrainingEvent(PlayerTrainingResponse playerTrainingResponse);

    @InheritInverseConfiguration
    PlayerTrainingResponse convertPlayerTrainingEventToPlayerTrainingResponse(PlayerTrainingEvent playerTrainingEvent);
}
