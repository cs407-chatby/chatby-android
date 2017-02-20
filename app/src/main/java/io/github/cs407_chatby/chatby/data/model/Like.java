package io.github.cs407_chatby.chatby.data.model;

import lombok.Data;
import lombok.experimental.Wither;

@Data
public class Like {
    private final ResourceUrl url;
    private final ResourceUrl user;
    private final ResourceUrl message;
}
