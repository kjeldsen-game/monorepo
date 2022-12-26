package com.kjeldsen.player.persistence.mongo.documents;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerActualSkills;
import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerName;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@Document(collection = "Players")
@TypeAlias("Player")
public class PlayerDocument {

    public static PlayerDocument from(Player player) {
        Map<String, Integer> mappedSkills = player.getActualSkills().values().entrySet()
            .stream()
            .map(entry -> Map.entry(entry.getKey().name(), entry.getValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return PlayerDocument.builder()
            .id(player.getId().value())
            .name(player.getName().value())
            .age(player.getAge().value())
            .position(player.getPosition().name())
            .skills(mappedSkills)
            .build();
    }

    @Id
    private String id;
    private String name;
    private Integer age;
    private String position;
    private Map<String, Integer> skills;

    public Player toDomain() {
        Map<PlayerSkill, Integer> mappedSkills = skills.entrySet()
            .stream()
            .map(entry -> Map.entry(PlayerSkill.valueOf(entry.getKey()), entry.getValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Player.builder()
            .id(PlayerId.of(id))
            .name(PlayerName.of(name))
            .age(PlayerAge.of(age))
            .position(PlayerPosition.valueOf(position))
            .actualSkills(PlayerActualSkills.of(mappedSkills))
            .build();
    }
}
