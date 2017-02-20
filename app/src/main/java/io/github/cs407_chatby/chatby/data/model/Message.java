package io.github.cs407_chatby.chatby.data.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Wither;

@Data
public class Message {
    private final ResourceUrl url;
    private final Date creationTime;
    @Wither private final Boolean anonymous;
    @Wither private final String content;
    private final ResourceUrl createdBy;
    private final ResourceUrl room;
    private final List<ResourceUrl> likes;

    public List<ResourceUrl> getLikes() {
        return Collections.unmodifiableList(likes);
    }
}
