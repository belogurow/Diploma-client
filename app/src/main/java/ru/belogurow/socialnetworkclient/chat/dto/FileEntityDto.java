package ru.belogurow.socialnetworkclient.chat.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import ru.belogurow.socialnetworkclient.chat.model.FileType;

public class FileEntityDto implements Serializable{

    private UUID id;
    private UUID authorId;
    private String title;
    private String dataUrl;
    private Date updateTime;
    private FileType fileType;

    public FileEntityDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FileEntityDto that = (FileEntityDto) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(authorId, that.authorId)
                .append(title, that.title)
                .append(dataUrl, that.dataUrl)
                .append(updateTime, that.updateTime)
                .append(fileType, that.fileType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(authorId)
                .append(title)
                .append(dataUrl)
                .append(updateTime)
                .append(fileType)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("authorId", authorId)
                .append("title", title)
                .append("dataUrl", dataUrl)
                .append("updateTime", updateTime)
                .append("fileType", fileType)
                .toString();
    }
}
