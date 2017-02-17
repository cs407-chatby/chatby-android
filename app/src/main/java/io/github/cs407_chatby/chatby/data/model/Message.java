package io.github.cs407_chatby.chatby.data.model;

import java.util.Date;

import lombok.Data;

@Data
public class Message {
    public final Integer id;
    public final Date creationTime;
    public final String content;
    public final Integer createdBy;
    public final Integer room;
}
