package com.kjeldsen.auth.domain.clients.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Builder
public class TeamDTO {

    private String id;
    private String name;
    private String leagueId;
}

