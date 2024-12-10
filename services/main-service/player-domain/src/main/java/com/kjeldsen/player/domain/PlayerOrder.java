package com.kjeldsen.player.domain;

public enum PlayerOrder {

    /*
     * Each player can receive an order from the manager. The order will affect how the player
     * carries out his action. If an order cannot be carried out because the team has not been,
     * configured correctly, or any other reason, then the order will not be applied and the default
     * action will be used as a fallback.
     */

    // Midfield orders

    PASS_FORWARD,
    LONG_SHOT,
    CHANGE_FLANK,
    PASS_TO_AREA,
    DRIBBLE_TO_AREA,
    WALL_PASS,

    // Forward orders

    // TODO

    // Disabled for player - NONE is also the default order

    NONE
}
