package com.kjeldsen.auth.domain.models;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Profile {
    @Email
    private String email;
    private String teamName;
    private String avatar;
}
