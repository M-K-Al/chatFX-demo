package models;

public class ImageMessage extends FileMessage {
    private ImageMessage() {
    }

    public ImageMessage(String id, Researcher sender, String uri) {
        super(ItemType.IMAGE, id, sender, uri);
    }

    public enum Type {

        MP4("jpg");

        private final String EXTENSION;

        Type(final String extension) {
            EXTENSION = extension;
        }

        public String getExtension() {
            return EXTENSION;
        }
    }
}