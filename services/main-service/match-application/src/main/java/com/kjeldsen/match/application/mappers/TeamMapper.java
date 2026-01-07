package com.kjeldsen.match.application.mappers;

import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.modifers.TeamModifiers;
import com.kjeldsen.player.rest.model.TeamResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {PlayerMapper.class})
public interface TeamMapper {

    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    @Mapping(source = "teamModifiers", target = "modifiers")
    Team mapTeamResponse(TeamResponse teamResponse);

    TeamModifiers mapTeamResponseTeamModifiers(com.kjeldsen.player.rest.model.TeamModifiers teamModifiers);

}
