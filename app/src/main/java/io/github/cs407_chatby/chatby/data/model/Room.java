package io.github.cs407_chatby.chatby.data.model;

import java.util.Date;

import lombok.Data;

@Data
public class Room {
    public final Integer id;
    public final String name;
    public final Date creationTime;
    public final Double radius;
    public final Date expireTime;
    public final String imageUrl;
    public final Double latitude;
    public final Double longitude;
    public final Integer createdBy;
}
