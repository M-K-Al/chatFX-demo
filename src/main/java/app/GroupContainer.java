package app;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import io.github.palexdev.materialfx.beans.PositionBean;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.effects.ripple.MFXCircleRippleGenerator;
import io.github.palexdev.materialfx.utils.NodeUtils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import utils.ThreadUtils;

import java.util.concurrent.TimeUnit;

import static de.jensd.fx.glyphs.materialicons.MaterialIcon.MORE_VERT;
import static de.jensd.fx.glyphs.materialicons.MaterialIcon.VERIFIED_USER;

public class GroupContainer extends BorderPane {

    public GroupContainer() {
        getStylesheets().add("group-container.css");
        setOnMousePressed(event -> requestFocus());
        initTop();
        setCenter(new ChatContainer());
    }

    private void initTop() {

        final BorderPane root = new BorderPane();
        BorderPane.setMargin(root, new Insets(5));

        ///////////////////////////////// Group icon /////////////////////////////////

        //noinspection ConstantConditions
        final ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/imgs/icon.jpg")));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(110);
        imageView.setSmooth(true);

        final Pane icon = new Pane(imageView);
        NodeUtils.makeRegionCircular(icon, 30);

        final MFXCircleRippleGenerator generator = new MFXCircleRippleGenerator(icon);
        generator.setRipplePositionFunction(mouseEvent -> new PositionBean(mouseEvent.getX(), mouseEvent.getY()));
        generator.setAnimationSpeed(3);

        icon.addEventHandler(MouseEvent.MOUSE_PRESSED, generator::generateRipple);
        icon.getChildren().add(generator);

        ///////////////////////////////// Group title /////////////////////////////////

        final Label title = new Label("Researchers Group");
        title.getStyleClass().add("group-title");

        final HBox left = new HBox(icon, title);
        left.setAlignment(Pos.CENTER);

        BorderPane.setMargin(left, new Insets(0, 0, 0, 20));

        root.setLeft(left);

        ///////////////////////////////// Right icons /////////////////////////////////

        final MFXIconWrapper more = new MFXIconWrapper(new MaterialIconView(MORE_VERT, "22"), 26)
                .defaultRippleGeneratorBehavior();
        more.getRippleGenerator().setAnimationSpeed(2);
        NodeUtils.makeRegionCircular(more);

        final MFXIconWrapper secure = new MFXIconWrapper(new MaterialIconView(VERIFIED_USER, "18"), 26)
                .defaultRippleGeneratorBehavior();
        secure.getRippleGenerator().setAnimationSpeed(2);
        secure.getStyleClass().add("secure-icon");
        NodeUtils.makeRegionCircular(secure);
        secure.setOnMousePressed(event -> showTooltip((Stage) getScene().getWindow(), secure, "Secure"));

        final HBox right = new HBox(5, secure, more);
        right.setAlignment(Pos.CENTER);
        root.setRight(right);

        setTop(root);
    }
    public static void showTooltip(@NotNull Stage owner, @NotNull Node control, String tooltipText) {
        final Point2D p = control.localToScene(0.0, 0.0);

        final Tooltip tooltip = new Tooltip();
        tooltip.setText(tooltipText);
        tooltip.setAutoHide(true);

        tooltip.show(owner,
                p.getX() + control.getScene().getX() + control.getScene().getWindow().getX() - 10,
                p.getY() + control.getScene().getY() + control.getScene().getWindow().getY() + 30);

        ThreadUtils.getDaemonScheduledThreadPoolExecutor(1).schedule(
                () -> Platform.runLater(tooltip::hide), 1, TimeUnit.SECONDS
        );
    }
}
