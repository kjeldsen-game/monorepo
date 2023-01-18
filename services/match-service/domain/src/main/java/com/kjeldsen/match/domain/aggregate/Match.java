package com.kjeldsen.match.domain.aggregate;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Builder
@Getter
public class Match {

    private String matchId;
    private List<Play> plays;

    public boolean isInitialPlay() {
        return CollectionUtils.isEmpty(plays);
    }

}
