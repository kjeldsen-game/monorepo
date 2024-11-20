package com.kjeldsen.auth;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Data
@Document(collection = "User")
public class User {

    private String id;
    private String email;
    private String password;
    private String teamId;
    private Set<Role> roles;


    // TODO roles - for now everyone is a `user`

//    public List<String> getRoles() {
//        return roles.stream()
//            .map(Role::name)
//            .toList();
//    }

    public Set<String> getPermissions() {
        return Role.getPermissions(Role.USER);
    }

}
