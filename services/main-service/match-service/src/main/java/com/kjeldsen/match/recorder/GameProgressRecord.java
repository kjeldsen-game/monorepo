package com.kjeldsen.match.recorder;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
@Builder
public class GameProgressRecord {

    public enum DuelStage {
        UNSPECIFIED,
        BEFORE,
        DURING,
        AFTER,
    }

    public enum Type {
        UNSPECIFIED,
        SUMMARY,
        INFORMATIVE,
        CALCULATION,
        ENTITY_DETAIL,
        ENTITY_BEHAVIOUR,
        BALL_BEHAVIOUR,
    }

    int clock;
    String detail;
    DuelStage duelState;
    Type type;

}
