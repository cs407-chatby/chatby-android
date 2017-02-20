package io.github.cs407_chatby.chatby.data.model;

import android.support.annotation.Nullable;

import java.util.Date;

import lombok.Data;

@Data
public class PostRoom {
    private final String name;
    private final Double radius;
    @Nullable private final Date expireTime;
    private final String imageUrl;
    private final Double latitude;
    private final Double longitude;
}
