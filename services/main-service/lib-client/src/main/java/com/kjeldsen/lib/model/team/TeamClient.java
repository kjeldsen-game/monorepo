package com.kjeldsen.lib.model.team;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kjeldsen.lib.deserializers.FansDeserializerClient;
import com.kjeldsen.lib.model.player.PlayerClient;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamClient {

    private String id;
    private String name;
    private String leagueId;
    private TeamModifiersClient teamModifiers;
    private BuildingsClient buildings;
    @JsonDeserialize(using = FansDeserializerClient.class)
    private FansClient fans;
    private List<PlayerClient> players;
}
