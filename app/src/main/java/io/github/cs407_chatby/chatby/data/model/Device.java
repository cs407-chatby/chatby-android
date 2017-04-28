package io.github.cs407_chatby.chatby.data.model;

import lombok.Data;

@Data
public class Device {
    private final ResourceUrl url;
    private final ResourceUrl user;
    private final String device;

    public Integer getId() {
        return url.getId();
    }
}
