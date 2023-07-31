package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.rest.model.PlayerPositionTendencyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper(uses = {IdMapper.class, PlayerMapper.class})
public interface PlayerPositionTendencyMapper {

    PlayerPositionTendencyMapper INSTANCE = Mappers.getMapper(PlayerPositionTendencyMapper.class);

    PlayerPositionTendencyResponse map(PlayerPositionTendency playerPositionTendency);

    Map<PlayerSkill, Integer> map(Map<String, Integer> tendencies);

}
