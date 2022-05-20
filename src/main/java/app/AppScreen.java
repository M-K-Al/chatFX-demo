package app;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.Researcher;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static utils.UsersUtils.USERS;
import static utils.UsersUtils.setSelfUser;

public class AppScreen extends Scene {

    private final BorderPane ROOT = new BorderPane();

    private final IntegerProperty CURRENT_SENDER = new SimpleIntegerProperty(0);

    {
        ROOT.setBackground(Background.fill(Color.web("black", 0.85)));
        getStylesheets().add("app-screen.css");
        setRoot(ROOT);
    }

    public AppScreen() {
        super(new Group());
        initTop();
        initRight();
        ROOT.setCenter(new GroupContainer());
    }

    private void initTop() {
        final ToolBar toolBar = new ToolBar(
                getMenuItem("New"),
                getMenuItem("Open"),
                getMenuItem("Save"),
                getMenuItem("Settings"),
                getMenuItem("Help"),
                getMenuItem("About")
        );
        ROOT.setTop(toolBar);
    }

    private void initRight() {

        ///////////////////////////////// Users list /////////////////////////////////

        final MFXListView<Researcher> activeUsers = new MFXListView<>(USERS);
        activeUsers.setCellFactory(person -> new ResearcherCellFactory(activeUsers, person));
        activeUsers.setConverter(FunctionalStringConverter.to(r -> {
            if (r == null) return "";
            final String text = "%s (%s)".formatted(r.getFullName(), r.getEmail());
            final Text measure = new Text(text);
            measure.setFont(Font.font("Segoe UI Semibold", 12));
            activeUsers.setPrefWidth(Math.max(activeUsers.getPrefWidth(), measure.getBoundsInLocal().getWidth() + 100));
            return text;
        }));
        activeUsers.getStyleClass().add("r-list-view");

        final Label activeUsersTitle = new Label("Active users");
        activeUsersTitle.getStyleClass().add("active-users-title");

        final MFXButton decrement = new MFXButton("Decrement");
        decrement.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && CURRENT_SENDER.get() > 0) CURRENT_SENDER.set(CURRENT_SENDER.get() - 1);
        });
        final MFXButton increment = new MFXButton("Increment");
        increment.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && CURRENT_SENDER.get() < USERS.size())
                CURRENT_SENDER.set(CURRENT_SENDER.get() + 1);
        });

        final Label currentSenderTitle = new Label("Current sender ID");
        currentSenderTitle.setPadding(new Insets(10));
        currentSenderTitle.setFont(Font.font(14));

        final Label currentSender = new Label();
        currentSender.setFont(Font.font(15));
        currentSender.setPadding(new Insets(5));
        currentSender.textProperty().bind(Bindings.concat("ID: ", CURRENT_SENDER));

        CURRENT_SENDER.addListener((observable, oldValue, newValue) -> setSelfUser(USERS.get(newValue.intValue())));

        final HBox currentSenderBox = new HBox(10, decrement, currentSender, increment);
        currentSenderBox.setAlignment(Pos.CENTER);
        VBox.setMargin(currentSenderBox, new Insets(10, 0, 30, 0));

        final VBox listBox = new VBox(activeUsersTitle, activeUsers, currentSenderTitle, currentSenderBox);

        activeUsers.prefHeightProperty().bind(listBox.heightProperty());
        ROOT.setRight(listBox);
    }

    @Contract("_ -> new")
    private @NotNull Node getMenuItem(@NotNull final String text) {
        final MFXButton button = new MFXButton(text);
        button.getStyleClass().add("r-menu-item");
        return button;
    }

    private static class ResearcherCellFactory extends MFXListCell<Researcher> {

        public ResearcherCellFactory(@NotNull final MFXListView<Researcher> listView, final Researcher researcher) {
            super(listView, researcher);
        }

        @Override
        protected void render(final Researcher researcher) {
            super.render(researcher);
            getChildren().add(0, new MaterialIconView(MaterialIcon.PERSON, "22"));
        }

        @Override
        public void updateItem(Researcher researcher) {
            super.updateItem(researcher);
            getChildren().get(0).setId(researcher.getOnlineSince() == null ? "offline" : "online");
        }
    }
}
