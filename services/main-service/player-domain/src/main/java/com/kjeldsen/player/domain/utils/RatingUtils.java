package com.kjeldsen.player.domain.utils;

import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;

import java.util.EnumMap;
import java.util.Map;

public class RatingUtils {

    public static final PlayerSkill[] DEFAULT_SKILL_ORDER = {
        PlayerSkill.SCORING,
        PlayerSkill.OFFENSIVE_POSITIONING,
        PlayerSkill.BALL_CONTROL,
        PlayerSkill.PASSING,
        PlayerSkill.AERIAL,
        PlayerSkill.CONSTITUTION,
        PlayerSkill.TACKLING,
        PlayerSkill.DEFENSIVE_POSITIONING
    };

    public static final Map<PlayerSkill, Double> CB_RATING_MAP = RatingUtils.createRatingMap(
        new Double[]{0.0, 0.0, 0.0, 0.0, 14.0, 19.6, 33.2, 33.2}
    );
    public static final Map<PlayerSkill, Double> LB_RB_RATING_MAP = RatingUtils.createRatingMap(
        new Double[]{0.0, 0.0, 0.0, 12.4, 17.4, 11.4, 29.4, 29.4}
    );
    public static final Map<PlayerSkill, Double> LWB_RWB_RATING_MAP = RatingUtils.createRatingMap(
        new Double[]{0.0, 14.0, 19.7, 12.9, 0.0, 14.0, 19.7, 19.7}
    );
    public static final Map<PlayerSkill, Double> DM_RATING_MAP = RatingUtils.createRatingMap(
        new Double[]{0.0, 0.0, 0.0, 19.6, 0.0, 14.0, 33.2, 33.2}
    );
    public static final Map<PlayerSkill, Double> CM_LM_RM_RATING_MAP = RatingUtils.createRatingMap(
        new Double[]{0.0, 16.3, 16.3, 33.3, 0.0, 11.3, 11.3, 11.3}
    );
    public static final Map<PlayerSkill, Double> OM_LW_RW_RATING_MAP = RatingUtils.createRatingMap(
        new Double[]{15.0, 24.9, 25.0, 25.0, 0.0, 10.2, 0.0, 0.0}
    );
    public static final Map<PlayerSkill, Double> FW_ST_RATING_MAP = RatingUtils.createRatingMap(
        new Double[]{40.0, 25.0, 25.0, 4.0, 3.0, 3.0, 0.0, 0.0}
    );
    public static final Map<PlayerSkill, Double> GK_RATING_MAP = RatingUtils.createRatingMap(
        new Double[]{30.0, 30.0, 10.0, 10.0, 10.0, 10.0}, new PlayerSkill[] {PlayerSkill.REFLEXES, PlayerSkill.GOALKEEPER_POSITIONING,
            PlayerSkill.INTERCEPTIONS, PlayerSkill.CONTROL, PlayerSkill.ORGANIZATION, PlayerSkill.ONE_ON_ONE}
    );

    /**
     * Returns the rating weight map corresponding to the given player position.
     */
    public static Map<PlayerSkill, Double> getRatingMapForPosition(PlayerPosition position) {
        if (position == null) {
            throw new NullPointerException("Position cannot be null");
        }
        return switch (position) {
            case FORWARD, STRIKER -> FW_ST_RATING_MAP;
            case LEFT_WINGER, OFFENSIVE_MIDFIELDER, RIGHT_WINGER -> OM_LW_RW_RATING_MAP;
            case CENTRE_MIDFIELDER, LEFT_MIDFIELDER, RIGHT_MIDFIELDER -> CM_LM_RM_RATING_MAP;
            case DEFENSIVE_MIDFIELDER -> DM_RATING_MAP;
            case LEFT_WINGBACK, RIGHT_WINGBACK -> LWB_RWB_RATING_MAP;
            case LEFT_BACK, RIGHT_BACK -> LB_RB_RATING_MAP;
            case CENTRE_BACK -> CB_RATING_MAP;
            case GOALKEEPER -> GK_RATING_MAP;
            default -> throw new IllegalArgumentException("Unexpected position: " + position);
        };
    }

    /**
     * Create a rating map with values matched to keys.
     * If keys are not provided, uses the default key order (for PlayerSkill).
     */
    public static <E extends Enum<E>> Map<E, Double> createRatingMap(
        Double[] values, E[] keyOrder) {

        if (keyOrder == null) {
            if (DEFAULT_SKILL_ORDER.length != values.length) {
                throw new IllegalArgumentException(
                    "Number of values must match default skill order: " + DEFAULT_SKILL_ORDER.length
                );
            }
            @SuppressWarnings("unchecked")
            E[] keys = (E[]) DEFAULT_SKILL_ORDER;
            keyOrder = keys;
        }

        if (values.length != keyOrder.length) {
            throw new IllegalArgumentException(
                "Number of values must match number of keys: " + keyOrder.length
            );
        }

        Map<E, Double> map = new EnumMap<>(keyOrder[0].getDeclaringClass());
        for (int i = 0; i < keyOrder.length; i++) {
            map.put(keyOrder[i], values[i]);
        }

        return map;
    }

    /**
     * Create a rating map with DEFAULT_SKILL_ORDER.
     */
    public static Map<PlayerSkill, Double> createRatingMap(Double[] values) {
        return createRatingMap(values, null);
    }
}
