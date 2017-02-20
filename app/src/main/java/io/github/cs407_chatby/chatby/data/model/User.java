package io.github.cs407_chatby.chatby.data.model;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Wither;

@Data
public class User {
    private final ResourceUrl<User> url;
    @Wither private final String username;
    @Wither private final String email;
    private final Boolean isStaff;
    @Wither private final String firstName;
    @Wither private final String lastName;
    private final Date dateJoined;
}

