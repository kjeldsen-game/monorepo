package com.kjeldsen.player.domain;

import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;

@Getter
public enum PlayerPosition {
    DEFENDER,
    MIDDLE,
    FORWARD;

    public static PlayerPosition random() {
        return values()[RandomUtils.nextInt(0, values().length)];
    }
}
