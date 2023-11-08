package com.kjeldsen.match.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import com.kjeldsen.match.engine.entities.SkillType;
import com.kjeldsen.match.engine.entities.duel.DuelRole;
import com.kjeldsen.match.engine.entities.duel.DuelType;
import com.kjeldsen.match.utils.JsonUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@Table(name = "player")
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Player {

    public static final int MAX_SKILL_VALUE = 100;
    public static final int MIN_SKILL_VALUE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    PlayerPosition position;

    @ElementCollection
    Map<SkillType, Integer> skills;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Team team;

    @Transient
    @JsonInclude
    Long teamId;

    @SuppressWarnings("unused")
    public Long getTeamId() {
        if (team != null) {
            return team.getId();
        } else {
            return null;
        }
    }

    // Instead of accessing the skill points directly, this method should be used to determine the
    // skill level of the player via the duel logic
    public Integer duelSkill(DuelType duelType, DuelRole role) {
        return skills.get(duelType.requiredSkill(role));
    }

    @Override
    public String toString() {
        return JsonUtils.prettyPrint(this);
    }
}
