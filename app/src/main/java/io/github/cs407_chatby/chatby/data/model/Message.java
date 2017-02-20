package io.github.cs407_chatby.chatby.data.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Wither;

@Data
public class Message {
    private final ResourceUrl<Message> url;
    private final Date creationTime;
    @Wither private final Boolean anonymous;
    @Wither private final String content;
    private final ResourceUrl<User> createdBy;
    private final ResourceUrl<Room> room;
    private final List<ResourceUrl<User>> likes;

    public List<ResourceUrl<User>> getLikes() {
        return Collections.unmodifiableList(likes);
    }
}
