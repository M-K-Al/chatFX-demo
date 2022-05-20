package runner;

import app.AppScreen;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class Main extends Application {

    @Override
    public void start(@NotNull Stage primaryStage) {
        primaryStage.setScene(new AppScreen());
        primaryStage.setTitle("Messaging Demo");
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(800);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
