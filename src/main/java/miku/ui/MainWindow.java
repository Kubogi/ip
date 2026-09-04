package miku.ui;

import java.net.URL;
import java.util.Objects;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import miku.Miku;

/** Controls the main JavaFX chat window for Miku. */
public class MainWindow extends AnchorPane {
    private static final Duration FAREWELL_DURATION = Duration.seconds(1);

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private final Image userImage = loadImage("/images/DaUser.png");
    private final Image mikuImage = loadImage("/images/DaMiku.png");
    private Miku miku;
    private Runnable exitHandler = () -> { };

    /** Configures automatic scrolling after FXML has injected the chat controls. */
    @FXML
    public void initialize() {
        dialogContainer.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
    }

    /** Supplies Miku's application logic and displays its opening greeting. */
    public void setMiku(Miku miku) {
        this.miku = Objects.requireNonNull(miku);
        dialogContainer.getChildren().add(DialogBox.getMikuDialog(miku.getWelcomeMessage(), mikuImage));
    }

    /** Supplies the action that runs after Miku's farewell has been shown. */
    public void setExitHandler(Runnable exitHandler) {
        this.exitHandler = Objects.requireNonNull(exitHandler);
    }

    /** Adds user input and Miku's response to the conversation before clearing the input field. */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (!input.isBlank()) {
            dialogContainer.getChildren().add(DialogBox.getUserDialog(input, userImage));
        }
        dialogContainer.getChildren().add(DialogBox.getMikuDialog(miku.getResponse(input), mikuImage));
        userInput.clear();
        if (miku.isExitRequested()) {
            closeAfterFarewell();
        }
    }

    /** Disables new messages and executes the exit action after the farewell is visible. */
    private void closeAfterFarewell() {
        userInput.setDisable(true);
        sendButton.setDisable(true);
        PauseTransition farewellPause = new PauseTransition(FAREWELL_DURATION);
        farewellPause.setOnFinished(event -> exitHandler.run());
        farewellPause.play();
    }

    /** Loads an optional avatar image, returning no image until the user supplies the resource. */
    private Image loadImage(String resourcePath) {
        URL imageResource = MainWindow.class.getResource(resourcePath);
        if (imageResource == null) {
            return null;
        }
        return new Image(imageResource.toExternalForm());
    }
}
