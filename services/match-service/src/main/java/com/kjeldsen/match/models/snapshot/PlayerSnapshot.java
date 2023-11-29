package com.kjeldsen.match.models.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kjeldsen.match.engine.entities.PlayerPosition;
import com.kjeldsen.match.engine.entities.SkillType;
import com.kjeldsen.match.engine.modifers.PlayerOrder;
import com.kjeldsen.match.models.Player;
import com.kjeldsen.match.models.Team;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "player_snapshot")
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class PlayerSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    Player player;

    String name;
    @Enumerated(EnumType.STRING)
    PlayerPosition position;

    @ElementCollection
    Map<SkillType, Integer> skills;

    @Enumerated(EnumType.STRING)
    PlayerOrder playerOrder;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    TeamSnapshot team;

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
}
