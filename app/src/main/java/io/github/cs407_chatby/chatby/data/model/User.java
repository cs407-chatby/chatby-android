package io.github.cs407_chatby.chatby.data.model;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Wither;

@Data
public class User {
    private final ResourceUrl url;
    @Wither private final String username;
    @Wither private final String email;
    private final Boolean isStaff;
    @Wither private final String firstName;
    @Wither private final String lastName;
    private final Date dateJoined;

    public Integer getId() {
        return url.getId();
    }
}

