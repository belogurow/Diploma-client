package ru.belogurow.socialnetworkclient.chat.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.UUID;

import ru.belogurow.socialnetworkclient.chat.model.ChatMessage;
import ru.belogurow.socialnetworkclient.users.model.User;

public class ChatRoomDto implements Serializable {

    private UUID id;
    private User firstUser;
    private User secondUser;
    private ChatMessage lastChatMessage;

    public ChatRoomDto(UUID id, User firstUser, User secondUser, ChatMessage lastChatMessage) {
        this.id = id;
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.lastChatMessage = lastChatMessage;
    }

    public ChatRoomDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(User firstUser) {
        this.firstUser = firstUser;
    }

    public User getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(User secondUser) {
        this.secondUser = secondUser;
    }

    public ChatMessage getLastChatMessage() {
        return lastChatMessage;
    }

    public void setLastChatMessage(ChatMessage lastChatMessage) {
        this.lastChatMessage = lastChatMessage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("firstUser", firstUser)
                .append("secondUser", secondUser)
                .append("lastChatMessage", lastChatMessage)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ChatRoomDto that = (ChatRoomDto) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(firstUser, that.firstUser)
                .append(secondUser, that.secondUser)
                .append(lastChatMessage, that.lastChatMessage)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(firstUser)
                .append(secondUser)
                .append(lastChatMessage)
                .toHashCode();
    }
}
