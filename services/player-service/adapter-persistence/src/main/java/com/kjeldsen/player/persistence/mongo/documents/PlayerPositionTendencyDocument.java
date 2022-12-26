package com.kjeldsen.player.persistence.mongo.documents;

import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;
import com.kjeldsen.player.domain.PlayerSkill;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Builder
@Getter
@Setter
@Document(collection = "PlayerPositionTendencies")
@TypeAlias("PlayerPositionTendency")
public class PlayerPositionTendencyDocument {
    public static PlayerPositionTendencyDocument from(PlayerPositionTendency playerPositionTendency) {
        return PlayerPositionTendencyDocument.builder()
            .position(playerPositionTendency.getPosition())
            .tendencies(playerPositionTendency.getTendencies())
            .build();
    }

    @Id
    private String id;
    @Indexed(unique = true)
    private PlayerPosition position;
    private Map<PlayerSkill, Integer> tendencies;

    public PlayerPositionTendency toDomain() {
        return PlayerPositionTendency.builder()
            .position(position)
            .tendencies(tendencies)
            .build();
    }
}
