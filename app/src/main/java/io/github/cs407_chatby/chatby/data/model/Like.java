package io.github.cs407_chatby.chatby.data.model;

import lombok.Data;
import lombok.experimental.Wither;

@Data
public class Like {
    private final ResourceUrl<Like> url;
    private final ResourceUrl<User> user;
    private final ResourceUrl<Message> message;
}
