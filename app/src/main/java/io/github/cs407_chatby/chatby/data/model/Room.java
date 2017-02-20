package io.github.cs407_chatby.chatby.data.model;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.experimental.Wither;

@Data
public class Room {
    private final ResourceUrl url;
    @Wither private final String name;
    private final Date creationTime;
    @Wither private final Double radius;
    @Wither @Nullable private final Date expireTime;
    @Wither private final String imageUrl;
    @Wither private final Double latitude;
    @Wither private final Double longitude;
    private final ResourceUrl createdBy;
    private final List<ResourceUrl> members;

    public List<ResourceUrl> getMembers() {
        return Collections.unmodifiableList(members);
    }
}
