package io.github.cs407_chatby.chatby.data.model;

import lombok.Data;

@Data
public class PostUser {
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String password;
}
