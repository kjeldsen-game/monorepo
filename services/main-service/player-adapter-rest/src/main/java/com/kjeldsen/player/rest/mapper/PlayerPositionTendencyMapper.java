package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import com.kjeldsen.player.rest.model.PlayerPositionTendencyResponse;
import com.kjeldsen.player.rest.model.PlayerPositionTendencyResponseTendenciesValue;
import com.kjeldsen.player.rest.model.UpdatePlayerPositionTendencyRequestValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.Map;

@Mapper(uses = {IdMapper.class, PlayerMapper.class})
public interface PlayerPositionTendencyMapper {
    PlayerPositionTendencyMapper INSTANCE = Mappers.getMapper(PlayerPositionTendencyMapper.class);

    PlayerPositionTendencyResponse map(PlayerPositionTendency playerPositionTendency);

    Map<PlayerSkill, PlayerSkills> map(Map<String, UpdatePlayerPositionTendencyRequestValue> tendencies);

    @Mapping(target = "actual", source = "value.actual")
    @Mapping(target = "potential", source = "value.potential")
    @Mapping(target = "playerSkillRelevance", source = "value.playerSkillRelevance")
    PlayerSkills map(UpdatePlayerPositionTendencyRequestValue value);


    default Map<String, PlayerPositionTendencyResponseTendenciesValue> madPlayerPositionTendencyResponseTendenciesValue(
        Map<PlayerSkill, PlayerSkills> map) {
        Map<String, PlayerPositionTendencyResponseTendenciesValue> result = new HashMap<>();
        map.forEach((key, value) -> {
            result.put(
                key.name(),
                mapPlayerPositionTendencyResponseTendenciesValue(key, value)
            );
        });
        return result;
    }

    default PlayerPositionTendencyResponseTendenciesValue mapPlayerPositionTendencyResponseTendenciesValue(PlayerSkill playerSkill,
        PlayerSkills skills) {
        return new PlayerPositionTendencyResponseTendenciesValue()
            .playerSkill(com.kjeldsen.player.rest.model.PlayerSkill.valueOf(playerSkill.name()))
            .playerSkills(PlayerMapper.INSTANCE.map(skills));
    }

}
