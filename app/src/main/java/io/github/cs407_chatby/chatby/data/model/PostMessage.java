package io.github.cs407_chatby.chatby.data.model;

import java.util.Date;

import lombok.Data;

@Data
public class PostMessage {
    public final String content;
    public final Integer room;
}
