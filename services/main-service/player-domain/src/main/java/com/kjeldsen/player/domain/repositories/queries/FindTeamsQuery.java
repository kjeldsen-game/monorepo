package com.kjeldsen.player.domain.repositories.queries;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FindTeamsQuery {

    private String userId;
    private String name;
    private int size;
    private int page;
}
