package com.kjeldsen.match.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@Entity
@Table(name = "match")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "home_id")
    Team home;
    @ManyToOne
    @JoinColumn(name = "away_id")
    Team away;

    @OneToOne(cascade = CascadeType.ALL)
    MatchReport matchReport;
}
