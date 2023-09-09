package com.kjeldsen.player.persistence.mongo.documents;

import com.kjeldsen.player.domain.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@Setter
@Document(collection = "Teams")
@TypeAlias("Team")
public class TeamDocument {

    public static TeamDocument from(Team team, String userId) {
        return TeamDocument.builder()
            .id(team.getId().value())
            .name(team.getName())
            .userId(userId)
            .build();
    }

    @Id
    private String id;
    private String name;
    private String userId;

}