package com.kjeldsen.match.domain.aggregate;

import com.kjeldsen.match.domain.id.TeamId;
import com.kjeldsen.match.domain.type.TeamStatus;
import lombok.Getter;

@Getter
public class Team {

    private TeamId id;
    private TeamStatus status;

}
