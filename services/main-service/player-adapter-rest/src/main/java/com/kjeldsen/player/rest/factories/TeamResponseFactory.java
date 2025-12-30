package com.kjeldsen.player.rest.factories;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.rest.mapper.team.TeamMapper;
import com.kjeldsen.player.rest.model.TeamResponse;
import com.kjeldsen.player.rest.views.ResponseView;
import org.springframework.stereotype.Component;

@Component
public class TeamResponseFactory {

    public TeamResponse create(ResponseView view, Team team) {
        System.out.println(view);
        return switch (view) {
            case PUBLIC -> TeamMapper.INSTANCE.mapToPublic(team);
            case PRIVATE -> TeamMapper.INSTANCE.mapToPrivate(team);
        };
    }
}
