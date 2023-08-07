package com.kjeldsen.player.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@Document(collection = "Teams")
@TypeAlias("Team")
public class Team {

    private TeamId id;
    private String userId;
    private String name;
    private List<Player> players;

    public record TeamId(String value) {
        public static TeamId generate() {
            return new TeamId(java.util.UUID.randomUUID().toString());
        }

        public static TeamId of(String id) {
            return new TeamId(id);
        }
    }
}
