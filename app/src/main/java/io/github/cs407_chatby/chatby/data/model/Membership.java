package io.github.cs407_chatby.chatby.data.model;

import lombok.Data;
import lombok.experimental.Wither;

@Data
public class Membership {
    private final ResourceUrl url;
    @Wither private final Boolean muted;
    private final ResourceUrl user;
    private final ResourceUrl room;

    public Integer getId() {
        return url.getId();
    }
}
