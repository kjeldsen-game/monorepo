package com.kjeldsen.player.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Team {

    private TeamId id;
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
