package ru.belogurow.socialnetworkclient.users.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.UUID;

public class FavoriteUsers implements Serializable{

    private UUID id;
    private UUID fromUserId;
    private UUID toUserId;

    public FavoriteUsers() {
    }

    public FavoriteUsers(UUID fromUserId, UUID toUserId) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(UUID fromUserId) {
        this.fromUserId = fromUserId;
    }

    public UUID getToUserId() {
        return toUserId;
    }

    public void setToUserId(UUID toUserId) {
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

        FavoriteUsers that = (FavoriteUsers) o;

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