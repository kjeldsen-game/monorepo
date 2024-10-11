package com.kjeldsen.player.domain;

public enum PlayerReceptionPreference {

    /*
     * Each player can have a reception preference. If a player receives better high balls,
     * the initiator of a pass should make a high pass.
     */
    DEMAND_LOW,
    DEMAND_HIGH,
    MIXED

}
