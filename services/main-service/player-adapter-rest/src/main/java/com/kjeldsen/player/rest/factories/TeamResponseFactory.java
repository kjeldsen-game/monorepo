package com.kjeldsen.player.rest.factories;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.rest.mapper.team.TeamMapper;
import com.kjeldsen.player.rest.model.TeamResponse;
import com.kjeldsen.player.rest.views.ResponseView;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamResponseFactory {

    public TeamResponse create(ResponseView view, Team team) {
        return switch (view) {
            case PUBLIC -> TeamMapper.INSTANCE.mapToPublic(team);
            case PRIVATE -> TeamMapper.INSTANCE.mapToPrivate(team);
        };
    }
}
