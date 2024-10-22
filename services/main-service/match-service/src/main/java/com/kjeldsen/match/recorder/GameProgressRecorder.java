package com.kjeldsen.match.recorder;

import com.kjeldsen.match.state.GameState;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@Builder
public class GameProgressRecorder {

    List<GameProgressRecord> record;

    public static GameProgressRecorder init() {
        return GameProgressRecorder.builder().record(new ArrayList<>()).build();
    }

    public void record(String detail, GameState state, GameProgressRecord.Type type, GameProgressRecord.DuelStage duelStage) {

        if (List.of(GameProgressRecord.Type.SUMMARY).contains(type)) {
            System.out.println("Clock " + state.getClock() + " - " + detail);
        }

        GameProgressRecord newRecord = GameProgressRecord.builder().detail(detail).clock(state.getClock()).type(type).duelState(duelStage).build();
        record.add(newRecord);
    }

}
