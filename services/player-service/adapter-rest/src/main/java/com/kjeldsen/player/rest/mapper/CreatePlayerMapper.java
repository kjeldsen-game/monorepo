package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.application.usecases.CreatePlayerUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.rest.model.CreatePlayerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreatePlayerMapper {

    CreatePlayerMapper INSTANCE = Mappers.getMapper(CreatePlayerMapper.class);

    @Mapping(target = "teamId", ignore = true)
    CreatePlayerUseCase.NewPlayer map(CreatePlayerRequest createPlayerRequest);

    default String map(Player.PlayerId playerId) {
        return playerId.value();
    }

}
