package app;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.utils.NodeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import models.ChatItem;
import models.FileMessage;
import models.ImageMessage;
import models.TextMessage;
import org.jetbrains.annotations.NotNull;
import utils.ChatItemsUtils;
import utils.SystemUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static de.jensd.fx.glyphs.materialicons.MaterialIcon.ATTACHMENT;
import static de.jensd.fx.glyphs.materialicons.MaterialIcon.SEND;
import static utils.UsersUtils.SELF_USER;
import static utils.UsersUtils.USERS;

public class ChatContainer extends BorderPane {
    private static final ObservableList<ChatItem> MESSAGES = FXCollections.observableArrayList();

    public ChatContainer() {
        getStylesheets().add("chat-container.css");
        setOnMousePressed(event -> requestFocus());
        initBottom();
        initCenter();
    }

    private void initBottom() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("All", "*.docx", "*.xlsx", "*.pdf", "*.mp4", "*.jpg"),
                new ExtensionFilter("Image", "*.jpg"),
                new ExtensionFilter("Video", "*.mp4"),
                new ExtensionFilter("Document", "*.docx", "*.xlsx", "*.pdf")
        );

        final TextField messageField = new TextField();
        messageField.getStyleClass().add("message-field");
        HBox.setHgrow(messageField, Priority.ALWAYS);
        messageField.setPromptText("Message...");
        messageField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) sendTextMessage(messageField);
        });

        final MaterialIconView uploadIcon = new MaterialIconView(ATTACHMENT, "24");
        uploadIcon.setRotate(-45);

        final MFXIconWrapper uploadContainer = new MFXIconWrapper(uploadIcon, 48).defaultRippleGeneratorBehavior();
        uploadContainer.getStyleClass().add("upload-container");
        uploadContainer.setOnMousePressed(event -> {
            if (!event.isPrimaryButtonDown()) return;
            final File file = fileChooser.showOpenDialog(getScene().getWindow());
            if (file == null) return;
            switch (ChatItemsUtils.getTypeFromExtension(getExtension(file.getPath()))) {
                case IMAGE -> MESSAGES.add(new ImageMessage(UUID.randomUUID().toString(), SELF_USER, file.getPath()));
                case FILE -> MESSAGES.add(new FileMessage(UUID.randomUUID().toString(), SELF_USER, file.getPath()));
            }
        });

        final MFXIconWrapper sendContainer = new MFXIconWrapper(new MaterialIconView(SEND, "24"), 48)
                .defaultRippleGeneratorBehavior();

        sendContainer.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown())
                sendTextMessage(messageField);
        });

        sendContainer.getStyleClass().add("send-container");
        NodeUtils.makeRegionCircular(sendContainer);
        BorderPane.setMargin(sendContainer, new Insets(0, 0, 0, 14));

        final BorderPane root = new BorderPane(new HBox(messageField, uploadContainer));
        root.setPadding(new Insets(5, 5, 20, 40));
        root.setRight(sendContainer);

        setBottom(root);
    }

    private @NotNull String getExtension(final @NotNull String path) {
        return path.substring(path.lastIndexOf(".") + 1);
    }

    private void sendTextMessage(@NotNull TextField messageField) {
        if (messageField.getText().isEmpty()) return;
        MESSAGES.add(new TextMessage(UUID.randomUUID().toString(), SELF_USER, messageField.getText()));
        messageField.setText("");
    }

    private void initCenter() {
        final ListView<ChatItem> listView = new ListView<>(MESSAGES);
        listView.setSelectionModel(new NoSelectionModel<>());
        listView.setCellFactory(param -> new MessageCellFactory());
        BorderPane.setMargin(listView, new Insets(5, 5, 0, 40));

        setCenter(listView);
    }

    private static class MessageCellFactory extends ListCell<ChatItem> {

        private final PseudoClass HYPERLINK_CLASS = PseudoClass.getPseudoClass("hyperlink");
        private final PseudoClass SENDER_CLASS = PseudoClass.getPseudoClass("sender");
        private final PseudoClass IMAGE_CLASS = PseudoClass.getPseudoClass("image");
        private final DateFormat FORMAT = new SimpleDateFormat("hh:mm a");
        private final GridPane PANE = new GridPane();
        private final GridPane MESSAGE_CONTAINER = new GridPane();
        private final SVGPath MESSAGE_ARROW = new SVGPath();
        private final ImageView IMAGE_MESSAGE_CONTENT = new ImageView();
        private final Text TEXT_MESSAGE_CONTENT = new Text();
        private final Text CREATION_TIME = new Text();
        private final Text SENDER_NAME = new Text();
        private final Color SENDER_COLOR = Color.web("#00b3be");
        private final Color RECEIVED_COLOR = Color.web("#141414");

        public MessageCellFactory() {
            setPadding(new Insets(5));

            IMAGE_MESSAGE_CONTENT.setPreserveRatio(true);
            IMAGE_MESSAGE_CONTENT.setFitWidth(360);

            TEXT_MESSAGE_CONTENT.setFont(Font.font("Segoe UI Semibold", 14));
            CREATION_TIME.setFont(Font.font("Segoe UI Semibold", 11));
            SENDER_NAME.setFont(Font.font("Segoe UI Semibold", 13));

            TEXT_MESSAGE_CONTENT.setFill(Color.web("white", 0.95));
            CREATION_TIME.setFill(Color.web("white", 0.8));
            SENDER_NAME.setFill(Color.web("white", 0.95));

            MESSAGE_CONTAINER.add(CREATION_TIME, 1, 3);
            MESSAGE_CONTAINER.getStyleClass().add("message-container");
            MESSAGE_ARROW.setContent("M 0 0 L 8 0 C 14 0 11 -5 7 -9 C -1 -17 0 -16 0 -16");
            MESSAGE_ARROW.setRotate(180);

            PANE.add(MESSAGE_ARROW, 0, 0);
            PANE.add(MESSAGE_CONTAINER, 1, 0);

            GridPane.setMargin(MESSAGE_ARROW, new Insets(0, -1, 0, 0));
            GridPane.setMargin(TEXT_MESSAGE_CONTENT, new Insets(5, 3, 3, 3));
            GridPane.setValignment(MESSAGE_ARROW, VPos.TOP);
        }

        @Override
        protected void updateItem(final ChatItem item, boolean empty) {
            super.updateItem(item, empty);

            MESSAGE_CONTAINER.pseudoClassStateChanged(IMAGE_CLASS, false);
            TEXT_MESSAGE_CONTENT.pseudoClassStateChanged(HYPERLINK_CLASS, false);
            MESSAGE_CONTAINER.getChildren().removeAll(TEXT_MESSAGE_CONTENT, IMAGE_MESSAGE_CONTENT);

            if (item != null && !empty) {
                if (USERS.get(0).equals(item.getSender())) {
                    setAlignment(Pos.BASELINE_RIGHT);

                    IMAGE_MESSAGE_CONTENT.setScaleX(-1);
                    TEXT_MESSAGE_CONTENT.setScaleX(-1);
                    CREATION_TIME.setScaleX(-1);
                    SENDER_NAME.setScaleX(-1);
                    PANE.setScaleX(-1);

                    MESSAGE_ARROW.setFill(SENDER_COLOR);
                    MESSAGE_CONTAINER.pseudoClassStateChanged(SENDER_CLASS, true);
                    MESSAGE_CONTAINER.getChildren().remove(SENDER_NAME);

                    GridPane.setHalignment(CREATION_TIME, HPos.LEFT);
                } else {
                    setAlignment(Pos.BASELINE_LEFT);

                    IMAGE_MESSAGE_CONTENT.setScaleX(1);
                    TEXT_MESSAGE_CONTENT.setScaleX(1);
                    CREATION_TIME.setScaleX(1);
                    SENDER_NAME.setScaleX(1);
                    PANE.setScaleX(1);

                    MESSAGE_ARROW.setFill(RECEIVED_COLOR);
                    MESSAGE_CONTAINER.pseudoClassStateChanged(SENDER_CLASS, false);

                    GridPane.setHalignment(CREATION_TIME, HPos.RIGHT);

                    if (!MESSAGE_CONTAINER.getChildren().contains(SENDER_NAME))
                        MESSAGE_CONTAINER.add(SENDER_NAME, 0, 1, 2, 1);
                }

                if (item instanceof final TextMessage msg) {
                    TEXT_MESSAGE_CONTENT.setText(msg.getMessageContent());
                    if (!MESSAGE_CONTAINER.getChildren().contains(TEXT_MESSAGE_CONTENT))
                        MESSAGE_CONTAINER.add(TEXT_MESSAGE_CONTENT, 0, 2, 2, 1);
                } else if (item instanceof final ImageMessage img) {
                    MESSAGE_ARROW.setFill(Color.TRANSPARENT);
                    MESSAGE_CONTAINER.pseudoClassStateChanged(IMAGE_CLASS, true);
                    MESSAGE_CONTAINER.setBackground(Background.fill(Color.TRANSPARENT));
                    IMAGE_MESSAGE_CONTENT.setImage(new Image(img.getUri()));
                    if (!MESSAGE_CONTAINER.getChildren().contains(IMAGE_MESSAGE_CONTENT))
                        MESSAGE_CONTAINER.add(IMAGE_MESSAGE_CONTENT, 0, 2, 2, 1);
                } else if (item instanceof final FileMessage file) {
                    TEXT_MESSAGE_CONTENT.setText("Click to see the file.");
                    TEXT_MESSAGE_CONTENT.pseudoClassStateChanged(HYPERLINK_CLASS, true);
                    TEXT_MESSAGE_CONTENT.setOnMousePressed(event -> {
                        if (event.isPrimaryButtonDown()) SystemUtils.showFile(file.getUri());
                    });
                    if (!MESSAGE_CONTAINER.getChildren().contains(TEXT_MESSAGE_CONTENT)) {
                        MESSAGE_CONTAINER.add(TEXT_MESSAGE_CONTENT, 0, 2, 2, 1);
                    }
                }

                CREATION_TIME.setText(
                        FORMAT.format(item.getCreationTime() == null ? new Date() : item.getCreationTime())
                );
                SENDER_NAME.setText(item.getSender().getFullName());
                setGraphic(PANE);
            } else {
                setGraphic(null);
            }
        }
    }

    public static class NoSelectionModel<T> extends MultipleSelectionModel<T> {

        @Override
        public ObservableList<Integer> getSelectedIndices() {
            return FXCollections.emptyObservableList();
        }

        @Override
        public ObservableList<T> getSelectedItems() {
            return FXCollections.emptyObservableList();
        }

        @Override
        public void selectIndices(int index, int... indices) {
        }

        @Override
        public void selectAll() {
        }

        @Override
        public void selectFirst() {
        }

        @Override
        public void selectLast() {
        }

        @Override
        public void clearAndSelect(int index) {
        }

        @Override
        public void select(int index) {
        }

        @Override
        public void select(T obj) {
        }

        @Override
        public void clearSelection(int index) {
        }

        @Override
        public void clearSelection() {
        }

        @Override
        public boolean isSelected(int index) {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public void selectPrevious() {
        }

        @Override
        public void selectNext() {
        }
    }
}