package io.github.cs407_chatby.chatby.ui.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.github.cs407_chatby.chatby.data.model.Message;
import io.github.cs407_chatby.chatby.data.model.ResourceUrl;
import io.github.cs407_chatby.chatby.data.model.User;
import lombok.Data;
import lombok.experimental.Wither;

@Data
public class ViewMessage {
    private final ResourceUrl url;
    private final Date creationTime;
    @Wither private final Boolean anonymous;
    @Wither private final String content;
    private final User createdBy;
    private final ResourceUrl room;
    private final List<ResourceUrl> likes;

    public List<ResourceUrl> getLikes() {
        return Collections.unmodifiableList(likes);
    }

    public Integer getId() {
        return url.getId();
    }

    public ViewMessage(Message message, User user) {
        url = message.getUrl();
        creationTime = message.getCreationTime();
        anonymous = message.getAnonymous();
        content = message.getContent();
        createdBy = user;
        room = message.getRoom();
        likes = message.getLikes();
    }
}