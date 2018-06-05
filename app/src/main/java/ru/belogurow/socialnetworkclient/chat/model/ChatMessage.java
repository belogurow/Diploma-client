package ru.belogurow.socialnetworkclient.chat.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class ChatMessage implements Serializable{

    private UUID id;
    private UUID authorId;
    private UUID chatRoomId;
    private Date date;
    private String text;

    public ChatMessage(UUID id, UUID authorId, UUID chatRoomId, Date date, String text) {
        this.id = id;
        this.authorId = authorId;
        this.chatRoomId = chatRoomId;
        this.date = date;
        this.text = text;
    }

    public ChatMessage() {
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(UUID chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("authorId", authorId)
                .append("chatRoomId", chatRoomId)
                .append("date", date)
                .append("text", text)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ChatMessage that = (ChatMessage) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(authorId, that.authorId)
                .append(chatRoomId, that.chatRoomId)
                .append(date, that.date)
                .append(text, that.text)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(authorId)
                .append(chatRoomId)
                .append(date)
                .append(text)
                .toHashCode();
    }
}
