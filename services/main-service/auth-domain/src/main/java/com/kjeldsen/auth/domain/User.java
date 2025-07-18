package com.kjeldsen.auth.domain;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.beans.Encoder;
import java.util.Base64;
import java.util.Set;

@Data
@Builder
@Document(collection = "User")
public class User {

    private String id;
    @Email
    private String email;
    private String password;
    private String teamId;
    private Set<Role> roles;
    private byte[] avatar;

    public Set<String> getPermissions() {
        return Role.getPermissions(Role.USER);
    }

    public String convertBytesToString() {
        return this.avatar != null ? new String(Base64.getEncoder().encode(this.avatar)) : null;
    }
}
