package ru.belogurow.socialnetworkclient.chat.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.UUID;

import ru.belogurow.socialnetworkclient.users.dto.UserDto;

public class ChatRoomDto implements Serializable {

    private UUID id;
    private UserDto firstUser;
    private UserDto secondUser;
    private ChatMessageDto lastChatMessage;

    public ChatRoomDto(UUID id, UserDto firstUser, UserDto secondUser, ChatMessageDto lastChatMessage) {
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

    public UserDto getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(UserDto firstUser) {
        this.firstUser = firstUser;
    }

    public UserDto getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(UserDto secondUser) {
        this.secondUser = secondUser;
    }

    public ChatMessageDto getLastChatMessage() {
        return lastChatMessage;
    }

    public void setLastChatMessage(ChatMessageDto lastChatMessage) {
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
