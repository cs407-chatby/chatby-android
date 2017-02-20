package io.github.cs407_chatby.chatby.data.model;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
