package com.kjeldsen.player.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Builder
@Data
@ToString
public class Rating {
    private double actual;
    private double potential;

    public void setRatings(Map<String, Double> ratings) {
        this.setActual(ratings.get("actual"));
        this.setPotential(ratings.get("potential"));
    }
}