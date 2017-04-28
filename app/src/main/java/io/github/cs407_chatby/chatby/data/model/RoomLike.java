package io.github.cs407_chatby.chatby.data.model;

import lombok.Data;

@Data
public class RoomLike {
    private final ResourceUrl url;
    private final ResourceUrl user;
    private final ResourceUrl room;

    public Integer getId() {
        return url.getId();
    }
}
