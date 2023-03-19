package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerActualSkills;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.rest.model.PlayerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    @Mapping(target = "age", source = "age.value")
    @Mapping(target = "name", expression = "java(mapName(player))")
    PlayerResponse map(Player player);

    default String map(PlayerId playerId) {
        return playerId.value();
    }

    default String mapName(Player player) {
        return player.getName().value();
    }

    default Map<String, String> map(PlayerActualSkills playerActualSkills) {
        return playerActualSkills
            .values()
            .entrySet()
            .stream()
            .map(this::map)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    default Map.Entry<String, String> map(Map.Entry<PlayerSkill, Integer> entry) {
        return new AbstractMap.SimpleEntry<>(entry.getKey().toString(), entry.getValue().toString());
    }

}
