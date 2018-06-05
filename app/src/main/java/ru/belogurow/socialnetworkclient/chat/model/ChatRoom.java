package ru.belogurow.socialnetworkclient.chat.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.UUID;

public class ChatRoom implements Serializable{

    private UUID id;
    private UUID firstUserId;
    private UUID secondUserId;

    public ChatRoom() {
    }

    public ChatRoom(UUID firstUserId, UUID secondUserId) {
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(UUID firstUserId) {
        this.firstUserId = firstUserId;
    }

    public UUID getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(UUID secondUserId) {
        this.secondUserId = secondUserId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("firstUserId", firstUserId)
                .append("secondUserId", secondUserId)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ChatRoom chatRoom = (ChatRoom) o;

        return new EqualsBuilder()
                .append(id, chatRoom.id)
                .append(firstUserId, chatRoom.firstUserId)
                .append(secondUserId, chatRoom.secondUserId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(firstUserId)
                .append(secondUserId)
                .toHashCode();
    }
}
