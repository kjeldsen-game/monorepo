package com.kjeldsen.player.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlayerDecline {
    private int declineYearsOn;
    private int declineStartAge;
    private int declineSpeed;

}
