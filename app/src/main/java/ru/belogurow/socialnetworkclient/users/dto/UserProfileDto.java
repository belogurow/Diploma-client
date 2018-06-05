package ru.belogurow.socialnetworkclient.users.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import ru.belogurow.socialnetworkclient.chat.dto.FileEntityDto;
import ru.belogurow.socialnetworkclient.users.model.UserProfileRole;

public class UserProfileDto implements Serializable {
    private UUID id;
    private String profession;
    private String description;
    private UUID userId;
    private Date birthDate;
    private UserProfileRole role;
    private FileEntityDto avatarFile;

    public UserProfileDto() {
    }

    public UserProfileDto(UUID id, String profession, String description, UUID userId, Date birthDate, UserProfileRole role, FileEntityDto avatarFile) {
        this.id = id;
        this.profession = profession;
        this.description = description;
        this.userId = userId;
        this.birthDate = birthDate;
        this.role = role;
        this.avatarFile = avatarFile;
    }

    public UserProfileRole getRole() {
        return role;
    }

    public void setRole(UserProfileRole role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public FileEntityDto getAvatarFile() {
        return avatarFile;
    }

    public void setAvatarFile(FileEntityDto avatarFile) {
        this.avatarFile = avatarFile;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("profession", profession)
                .append("description", description)
                .append("userId", userId)
                .append("birthDate", birthDate)
                .append("role", role)
                .append("avatarFile", avatarFile)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserProfileDto that = (UserProfileDto) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(profession, that.profession)
                .append(description, that.description)
                .append(userId, that.userId)
                .append(birthDate, that.birthDate)
                .append(role, that.role)
                .append(avatarFile, that.avatarFile)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(profession)
                .append(description)
                .append(userId)
                .append(birthDate)
                .append(role)
                .append(avatarFile)
                .toHashCode();
    }
}
