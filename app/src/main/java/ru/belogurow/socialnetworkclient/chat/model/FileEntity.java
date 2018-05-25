package ru.belogurow.socialnetworkclient.chat.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.UUID;

public class FileEntity {

    private UUID id;

//    private UUID authorId;
    private String title;
    private byte[] data;
    private Date updateTime;
    private FileType fileType;

    public FileEntity() {
    }

    public FileEntity(String title, byte[] data, Date updateTime, FileType fileType) {
        this.title = title;
        this.data = data;
        this.updateTime = updateTime;
        this.fileType = fileType;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("data", data)
                .append("updateTime", updateTime)
                .append("fileType", fileType)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FileEntity that = (FileEntity) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(title, that.title)
                .append(data, that.data)
                .append(updateTime, that.updateTime)
                .append(fileType, that.fileType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(title)
                .append(data)
                .append(updateTime)
                .append(fileType)
                .toHashCode();
    }
}
