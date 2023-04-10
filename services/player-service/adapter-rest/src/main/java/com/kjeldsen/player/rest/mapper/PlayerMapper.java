package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerActualSkills;
import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerName;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.rest.model.PlayerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper( PlayerMapper.class );

    PlayerResponse map(Player player);

    default String map(PlayerId playerId) {
        return playerId.toString();
    }

    default Integer map(PlayerAge playerAge) {
        return playerAge.value();
    }

    default String map(PlayerName playerName) {
        return playerName.value();
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
