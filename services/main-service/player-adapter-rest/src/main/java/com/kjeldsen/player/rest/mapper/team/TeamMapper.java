package com.kjeldsen.player.rest.mapper.team;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.TeamModifiers;
import com.kjeldsen.player.rest.mapper.common.IdMapper;
import com.kjeldsen.player.rest.model.FanTier;
import com.kjeldsen.player.rest.model.TeamResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(uses = {IdMapper.class})
public interface TeamMapper {

    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    TeamResponse map(Team team);

    @Mapping(target = "economy", ignore = true)
    @Mapping(target = "buildings", ignore = true)
    @Mapping(target = "cantera", ignore = true)
    @Mapping(target = "fans", ignore = true)
    @Mapping(target = "leagueId", ignore = true)
    TeamResponse mapToPublic(Team team);

    TeamResponse mapToPrivate(Team team);

    default Map<String, FanTier> map(Team.Fans fans) {
        if (fans == null || fans.getFanTiers() == null) {
            return Collections.emptyMap();
        }

        return fans.getFanTiers().entrySet().stream()
            .collect(Collectors.toMap(
                entry -> String.valueOf(entry.getKey()),
                entry -> map(entry.getValue())
            ));
    }

    FanTier map(Team.Fans.FanTier fanTier);

    TeamModifiers map(com.kjeldsen.player.rest.model.TeamModifiers teamModifiers);
}
