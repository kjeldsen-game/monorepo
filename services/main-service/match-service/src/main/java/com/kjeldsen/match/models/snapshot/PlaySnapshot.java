package com.kjeldsen.match.models.snapshot;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kjeldsen.match.engine.entities.Action;
import com.kjeldsen.match.engine.entities.PitchArea;
import com.kjeldsen.match.engine.entities.duel.DuelOrigin;
import com.kjeldsen.match.engine.entities.duel.DuelResult;
import com.kjeldsen.match.models.DuelStats;
import com.kjeldsen.match.engine.entities.duel.DuelType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@Table(name = "play_snapshot")
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class PlaySnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    Action action;
    @Enumerated(EnumType.STRING)
    DuelType duelType;
    @Enumerated(EnumType.STRING)
    PitchArea pitchArea;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    PlayerSnapshot initiator;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    PlayerSnapshot challenger;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    PlayerSnapshot receiver;

    @Enumerated(EnumType.STRING)
    DuelResult result;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    DuelStats initiatorStats;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    DuelStats challengerStats;

    @Enumerated(EnumType.STRING)
    DuelOrigin origin;
    Integer clock;
}