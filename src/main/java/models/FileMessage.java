package models;

public class FileMessage extends ChatItem {

    protected String uri;

    protected FileMessage() {
    }

    public FileMessage(String id, Researcher sender, String uri) {
        super(ItemType.FILE, id, sender);
        this.uri = uri;
    }

    public FileMessage(ItemType type, String id, Researcher sender, String uri) {
        super(type, id, sender);
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public FileMessage setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public enum Type {

        MP4("mp4"),

        DOCX("docx"),

        XLSX("xlsx"),

        PDF("pdf");

        private final String EXTENSION;

        Type(final String extension) {
            EXTENSION = extension;
        }

        public String getExtension() {
            return EXTENSION;
        }
    }
}
