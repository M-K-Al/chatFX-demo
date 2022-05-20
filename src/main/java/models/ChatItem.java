package models;

import java.util.Date;
import java.util.Objects;

public abstract class ChatItem {

    protected String id;
    protected ItemType type;
    protected Date creationTime;
    protected Researcher sender;
    protected String messageSecret;

    public ChatItem() {
    }

    public ChatItem(ItemType type, String id, Researcher sender) {
        this.id = id;
        this.type = type;
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public ChatItem setId(String id) {
        this.id = id;
        return this;
    }

    public ItemType getType() {
        return type;
    }

    public ChatItem setType(ItemType type) {
        this.type = type;
        return this;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public ChatItem setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public Researcher getSender() {
        return sender;
    }

    public ChatItem setSender(Researcher sender) {
        this.sender = sender;
        return this;
    }

    public String getMessageSecret() {
        return messageSecret;
    }

    public ChatItem setMessageSecret(String messageSecret) {
        this.messageSecret = messageSecret;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return Objects.equals(id, ((ChatItem) o).id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public enum ItemType {

        TEXT,

        IMAGE,

        FILE,

        UNKNOWN

    }
}
