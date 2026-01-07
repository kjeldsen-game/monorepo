package com.kjeldsen.match.application.mappers;

import com.kjeldsen.match.domain.entities.Player;
import com.kjeldsen.match.rest.model.EditPlayerRequest;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.rest.model.PlayerResponse;
import com.kjeldsen.player.rest.model.PlayerResponseActualSkillsValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    List<Player> mapEditPlayerList(List<EditPlayerRequest> editPlayerRequestsList);

    @Mapping(source = "actualSkills", target = "skills", qualifiedByName = "mapActualSkills")
    Player mapPlayerResponse(PlayerResponse playerResponse);

    List<Player> mapPlayerResponseList(List<PlayerResponse> playerResponseList) ;

    @Named("mapActualSkills")
    default Map<PlayerSkill, Integer> mapActualSkills(Map<String, PlayerResponseActualSkillsValue> skills) {
        if (skills == null || skills.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<PlayerSkill, Integer> result = new EnumMap<>(PlayerSkill.class);
        skills.forEach((skillName, value) -> {
            if (value == null || value.getPlayerSkills() == null) {
                return;
            }

            try {
                PlayerSkill skill = PlayerSkill.valueOf(skillName);
                result.put(skill, value.getPlayerSkills().getActual());
            } catch (IllegalArgumentException ex) {}
        });
        return result;
    }
}
