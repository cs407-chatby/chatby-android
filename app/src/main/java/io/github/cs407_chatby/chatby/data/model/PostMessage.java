package io.github.cs407_chatby.chatby.data.model;

import lombok.Data;

@Data
public class PostMessage {
    private final Boolean anonymous;
    private final String content;
    private final ResourceUrl room;
}
