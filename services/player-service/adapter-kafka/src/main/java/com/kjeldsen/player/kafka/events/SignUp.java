package com.kjeldsen.player.kafka.events;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class SignUp {

    private String id;
    private String username;

}
