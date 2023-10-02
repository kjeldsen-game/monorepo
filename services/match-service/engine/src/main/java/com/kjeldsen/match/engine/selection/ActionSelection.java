package com.kjeldsen.match.engine.selection;

import com.kjeldsen.match.domain.type.Action;
import com.kjeldsen.match.engine.state.GameState;
import java.util.List;
import java.util.Random;

public class ActionSelection {

    /*
     * Selects individual actions from lists of valid actions. Note that the valid actions (which
     * are independent of strategies and modifiers) need to be supplied to this class and should not
     * be determined here.
     */

    // At present this is just a random selection
    public static Action selectAction(GameState state, List<Action> actions) {
        int size = actions.size();
        return actions.get(new Random().nextInt(size));
    }
}
