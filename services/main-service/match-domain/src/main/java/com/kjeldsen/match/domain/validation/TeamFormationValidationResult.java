package com.kjeldsen.match.domain.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class TeamFormationValidationResult {
    private Boolean valid;
    private List<TeamFormationValidationItem> items = new ArrayList<>();

    public void addInfo(String info) {
        this.items.add(new TeamFormationValidationItem(true, info));
    }

    public void addError(String error) {
        this.valid = false;
        this.items.add(new TeamFormationValidationItem(false, error));
    }
}
