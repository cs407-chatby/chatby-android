package io.github.cs407_chatby.chatby.data.model;

import lombok.Data;
import lombok.experimental.Wither;

@Data
public class Membership {
    private final ResourceUrl<Membership> url;
    @Wither private final Boolean muted;
    private final ResourceUrl<User> user;
    private final ResourceUrl<Room> room;
}
