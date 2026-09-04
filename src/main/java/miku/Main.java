package miku;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import miku.ui.MainWindow;

/** Provides the JavaFX application shell for Miku. */
public class Main extends Application {
    private static final double MINIMUM_WINDOW_HEIGHT = 220;
    private static final double MINIMUM_WINDOW_WIDTH = 417;

    private final Miku miku = new Miku();

    /** Loads Miku's FXML interface into the primary application stage. */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane mainLayout = fxmlLoader.load();
            MainWindow mainWindow = fxmlLoader.getController();
            mainWindow.setMiku(miku);
            mainWindow.setExitHandler(stage::close);

            Scene scene = new Scene(mainLayout);
            stage.setTitle("Miku");
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setScene(scene);
            stage.show();
        } catch (IOException exception) {
            throw new IllegalStateException("Miku's interface could not be loaded.", exception);
        }
    }
}
