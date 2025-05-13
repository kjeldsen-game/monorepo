package com.kjeldsen.lib.events;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserRegisterEvent extends Event {
    String teamName;
    Integer numberOfPlayers;
    String userId;
}