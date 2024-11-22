package com.kjeldsen.auth.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document(collection = "User")
public class User {

    private String id;
    private String email;
    private String password;
    private String teamId;
    private Set<Role> roles;

    public Set<String> getPermissions() {
        return Role.getPermissions(Role.USER);
    }

}
