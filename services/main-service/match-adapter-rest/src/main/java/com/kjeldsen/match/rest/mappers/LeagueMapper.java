package com.kjeldsen.match.rest.mappers;

import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.rest.model.LeagueResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {IdMapper.class})
public interface LeagueMapper {

    LeagueMapper INSTANCE = Mappers.getMapper(LeagueMapper.class);

    LeagueResponse leagueResponseMap(League league);
}
