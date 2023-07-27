package com.kjeldsen.player.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@ToString
@Document(collection = "PlayerSkillPoints")
@TypeAlias("PlayerSkillPoints")
public class PlayerSkillPoints {
    private Integer actualPoints;
    private Integer potencialPoints;

}
