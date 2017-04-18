package io.github.cs407_chatby.chatby.ui.viewModel;

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

    public static ViewMessage create(Message message, User user) {
        return new ViewMessage(message.getUrl(), message.getCreationTime(), message.getAnonymous(),
                message.getContent(), user, message.getRoom(), message.getLikes());
    }
}