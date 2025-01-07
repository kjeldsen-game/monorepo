package com.kjeldsen.match.rest.mapper;

import com.kjeldsen.match.entities.Team;
import com.kjeldsen.match.rest.model.TeamModifiers;
import com.kjeldsen.match.rest.model.TeamResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {IdMapper.class})
public interface TeamMapper {
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    TeamResponse map(Team team);

    com.kjeldsen.match.modifers.TeamModifiers map(TeamModifiers teamModifiers);

}
