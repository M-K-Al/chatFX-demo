package models;

public class TextMessage extends ChatItem {

    private String messageContent;

    public TextMessage() {
    }

    public TextMessage(String id, Researcher sender, String messageContent) {
        super(ItemType.TEXT, id, sender);
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public TextMessage setMessageContent(String messageContent) {
        this.messageContent = messageContent;
        return this;
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "id='" + id + '\'' +
                ", creationTime=" + creationTime +
                ", sender=" + sender +
                ", messageContent='" + messageContent + '\'' +
                "} " + super.toString();
    }
}
