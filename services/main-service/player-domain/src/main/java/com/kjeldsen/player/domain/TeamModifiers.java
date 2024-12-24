package com.kjeldsen.player.domain;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TeamModifiers {
    private Tactic tactic;
    private VerticalPressure verticalPressure;
    private HorizontalPressure horizontalPressure;

    public enum Tactic {
        DOUBLE_TEAM,
        MAN_ON_MAN,
        ZONE,
        COUNTER_ATTACK,
        POSSESSION_CONTROL,
        TIKA_TAKA,
        WING_PLAY,
        CATENACCIO,
        ROUTE_ONE,
        OFFSIDE_TRAP;
    }

        public enum VerticalPressure {

        MID_PRESSURE,
        LOW_PRESSURE,
        NO_VERTICAL_FOCUS;
    }

    public enum HorizontalPressure {

        SWARM_CENTRE,
        SWARM_FLANKS,
        NO_HORIZONTAL_FOCUS;
    }


    public static <T extends Enum<T>> T getRandomValueTeamModifier(Class<T> enumClass) {
        T[] enumConstants = enumClass.getEnumConstants();
        int randomIndex = (int) (Math.random() * enumConstants.length);
        return enumConstants[randomIndex];
    }

}

