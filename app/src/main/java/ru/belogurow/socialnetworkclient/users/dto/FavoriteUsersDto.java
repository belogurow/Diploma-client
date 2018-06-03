package ru.belogurow.socialnetworkclient.users.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

public class FavoriteUsersDto {

    private UUID id;
    private UserDto fromUserId;
    private UserDto toUserId;

    public FavoriteUsersDto(UUID id, UserDto fromUserId, UserDto toUserId) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }

    public FavoriteUsersDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserDto getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(UserDto fromUserId) {
        this.fromUserId = fromUserId;
    }

    public UserDto getToUserId() {
        return toUserId;
    }

    public void setToUserId(UserDto toUserId) {
        this.toUserId = toUserId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("fromUserId", fromUserId)
                .append("toUserId", toUserId)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FavoriteUsersDto that = (FavoriteUsersDto) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(fromUserId, that.fromUserId)
                .append(toUserId, that.toUserId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(fromUserId)
                .append(toUserId)
                .toHashCode();
    }
}

