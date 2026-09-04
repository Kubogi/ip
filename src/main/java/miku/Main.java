package miku;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/** Provides the JavaFX application shell for Miku. */
public class Main extends Application {
    /** Displays Miku's initial JavaFX window. */
    @Override
    public void start(Stage stage) {
        Label greeting = new Label("Hello! I'm Hatsune Miku ♪");
        Scene scene = new Scene(greeting, 400, 600);
        stage.setTitle("Miku");
        stage.setScene(scene);
        stage.show();
    }
}
