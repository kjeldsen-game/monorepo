package com.kjeldsen.match.domain.aggregate;

import com.kjeldsen.match.domain.id.MatchId;
import com.kjeldsen.match.domain.type.MatchModifier;
import com.kjeldsen.match.domain.type.ModifierWrapper;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;

@Builder
@Getter
public class Match {

    private MatchId matchId;
    private ImmutablePair<Team, Team> teams;
    private List<Opportunity> opportunities;
    private List<ModifierWrapper<MatchModifier>> modifiers;

    public boolean hasModifiers() {
        return !modifiers.isEmpty();
    }

}
