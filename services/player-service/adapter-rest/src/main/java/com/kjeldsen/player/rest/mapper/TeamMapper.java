package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.rest.model.TeamResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {IdMapper.class})
public interface TeamMapper {

    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    TeamResponse map(Team team);

}
