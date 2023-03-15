package com.kjeldsen.player.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlayerDecline {
    private boolean decline;
    private int declineYearsOn;
    private int declineStartAge;
    private int declineSpeed;

    public PlayerDecline(boolean decline) {
        this.decline = decline;
    }
}
