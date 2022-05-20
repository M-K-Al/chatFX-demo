package utils;

import models.FileMessage;
import models.ImageMessage;

import java.util.HashMap;
import java.util.Map;

import static models.ChatItem.ItemType;

public class ChatItemsUtils {

    private static final Map<String, ItemType> types = new HashMap<>();

    static {
        for (final ImageMessage.Type type : ImageMessage.Type.values()) types.put(type.getExtension(), ItemType.IMAGE);
        for (final FileMessage.Type type : FileMessage.Type.values()) types.put(type.getExtension(), ItemType.FILE);
    }

    public static ItemType getTypeFromExtension(final String extension) {
        return types.getOrDefault(extension, ItemType.UNKNOWN);
    }

}
