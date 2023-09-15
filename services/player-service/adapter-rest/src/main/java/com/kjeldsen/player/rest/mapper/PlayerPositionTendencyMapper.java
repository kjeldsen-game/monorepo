package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.rest.model.PlayerPositionTendencyResponse;
import com.kjeldsen.player.rest.model.PlayerSkills;
import com.kjeldsen.player.rest.model.UpdatePlayerPositionTendencyRequestValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper(uses = {IdMapper.class, PlayerMapper.class})
public interface PlayerPositionTendencyMapper {

    PlayerPositionTendencyMapper INSTANCE = Mappers.getMapper(PlayerPositionTendencyMapper.class);

    PlayerPositionTendencyResponse map(PlayerPositionTendency playerPositionTendency);

    Map<PlayerSkill, PlayerSkills> map(Map<String, UpdatePlayerPositionTendencyRequestValue> tendencies);

    @Mapping(target = "actual", source = "value.actual")
    @Mapping(target = "potential", source = "value.potential")
    PlayerSkills map(UpdatePlayerPositionTendencyRequestValue value);

}
