package com.kjeldsen.match.domain.event;

import com.kjeldsen.match.domain.id.DuelId;
import com.kjeldsen.match.domain.id.OpportunityId;
import com.kjeldsen.match.domain.id.PlayId;
import com.kjeldsen.match.domain.id.PlayerId;
import com.kjeldsen.match.domain.type.PlayAction;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class PlayStartedEvent extends Event {

    private PlayId playId;
    private OpportunityId opportunityId;
    private List<PlayerId> players;
    private PlayAction action;
    private List<DuelId> duelsIds;

}
