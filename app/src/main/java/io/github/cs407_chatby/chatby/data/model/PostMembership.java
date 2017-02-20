package io.github.cs407_chatby.chatby.data.model;

import lombok.Data;

@Data
public class PostMembership {
    private final Boolean muted;
    private final ResourceUrl<Room> room;
}
