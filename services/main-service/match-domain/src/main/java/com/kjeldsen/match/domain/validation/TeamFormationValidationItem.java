package com.kjeldsen.match.domain.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class TeamFormationValidationItem {
    private Boolean valid;
    private String message;
}
