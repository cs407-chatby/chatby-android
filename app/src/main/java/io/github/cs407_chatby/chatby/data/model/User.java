package io.github.cs407_chatby.chatby.data.model;

import java.util.Date;

import lombok.Data;

@Data
public class User {
    private final Integer id;
    private final String username;
    private final String email;
    private final Boolean isStaff;
    private final String firstName;
    private final String lastName;
    private final Date dateJoined;
}
