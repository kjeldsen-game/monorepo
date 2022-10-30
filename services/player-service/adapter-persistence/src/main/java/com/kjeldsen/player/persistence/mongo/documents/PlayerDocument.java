package com.kjeldsen.player.persistence.mongo.documents;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.PlayerName;
import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkills;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.stream.Collectors;

@Builder
@Data
@Document(collection = "Players")
public class PlayerDocument {

    public static PlayerDocument from(Player player) {
        Map<String, Integer> mappedSkills = player.getSkills().values().entrySet()
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
        Map<PlayerSkills.PlayerSkill, Integer> mappedSkills = skills.entrySet()
                .stream()
                .map(entry -> Map.entry(PlayerSkills.PlayerSkill.valueOf(entry.getKey()), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Player.builder()
                .id(PlayerId.of(id))
                .name(PlayerName.of(name))
                .age(PlayerAge.of(age))
                .position(PlayerPosition.valueOf(position))
                .skills(PlayerSkills.of(mappedSkills))
                .build();
    }
}
