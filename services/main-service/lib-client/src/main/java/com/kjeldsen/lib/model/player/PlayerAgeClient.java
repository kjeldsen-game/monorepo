package com.kjeldsen.lib.model.player;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerAgeClient {
    private Integer years;
    private Double months;
    private Double days;
}
