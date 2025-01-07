package com.kjeldsen.league.rest.mapper;

import com.kjeldsen.league.domain.League;
import com.kjeldsen.league.rest.model.LeagueResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {IdMapper.class})
public interface LeagueMapper {

    LeagueMapper INSTANCE = Mappers.getMapper(LeagueMapper.class);

    LeagueResponse leagueResponseMap(League league);
}
