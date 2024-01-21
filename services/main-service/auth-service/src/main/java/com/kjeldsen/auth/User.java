package com.kjeldsen.auth;

import java.util.List;
import java.util.Set;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "User")
public class User {

    private String id;
    private String email;
    private String password;
    private String teamId;

    // TODO roles - for now everyone is a `user`

    public List<String> getRoles() {
        return List.of(Role.USER.toString());
    }

    public Set<String> getPermissions() {
        return Role.getPermissions(Role.USER);
    }
}
