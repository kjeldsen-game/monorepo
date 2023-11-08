package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerSkill;
import com.kjeldsen.player.domain.PlayerSkills;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class UpdatePlayerTendencies {
    private PlayerPosition position;
    private Map<PlayerSkill, PlayerSkills> tendencies;
}
