package com.kjeldsen.player.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Player {

    private PlayerId id;
    private PlayerName name;
    private PlayerAge age;
    private PlayerPosition position;
    private PlayerAbilities abilities;
}
