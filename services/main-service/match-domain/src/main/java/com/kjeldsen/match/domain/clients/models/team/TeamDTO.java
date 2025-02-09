package com.kjeldsen.match.domain.clients.models.team;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kjeldsen.match.domain.clients.deserializers.FansDeserializer;
import com.kjeldsen.match.domain.clients.models.player.PlayerDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class TeamDTO {

    private String id;
    private String name;
    private String leagueId;
    private TeamModifiers teamModifiers;
    private Buildings buildings;
    @JsonDeserialize(using = FansDeserializer.class)
    private Fans fans;
    private List<PlayerDTO> players;
}
