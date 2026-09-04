package miku;

import java.net.URL;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import miku.ui.DialogBox;

/** Provides the JavaFX application shell for Miku. */
public class Main extends Application {
    private static final double WINDOW_WIDTH = 400;
    private static final double WINDOW_HEIGHT = 600;
    private static final double INPUT_HEIGHT = 43;
    private static final double SEND_BUTTON_WIDTH = 76;

    private final Image userImage = loadImage("/images/DaUser.png");
    private final Image mikuImage = loadImage("/images/DaMiku.png");

    /** Displays Miku's initial JavaFX window. */
    @Override
    public void start(Stage stage) {
        VBox dialogContainer = new VBox();
        ScrollPane scrollPane = new ScrollPane(dialogContainer);
        TextField userInput = new TextField();
        Button sendButton = new Button("Send ♪");
        AnchorPane mainLayout = new AnchorPane(scrollPane, userInput, sendButton);

        dialogContainer.getChildren().add(new DialogBox("Hello! I'm Hatsune Miku ♪", mikuImage));
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        userInput.setPromptText("Tell Miku what to do...");
        userInput.setPrefHeight(INPUT_HEIGHT);
        sendButton.setPrefHeight(INPUT_HEIGHT);
        sendButton.setPrefWidth(SEND_BUTTON_WIDTH);

        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, INPUT_HEIGHT);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(userInput, SEND_BUTTON_WIDTH);
        AnchorPane.setBottomAnchor(userInput, 0.0);
        AnchorPane.setLeftAnchor(userInput, 0.0);
        AnchorPane.setRightAnchor(sendButton, 0.0);
        AnchorPane.setBottomAnchor(sendButton, 0.0);

        Scene scene = new Scene(mainLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Miku");
        stage.setMinHeight(220);
        stage.setMinWidth(417);
        stage.setScene(scene);
        stage.show();
    }

    /** Loads an optional avatar image, returning no image until the user supplies the resource. */
    private Image loadImage(String resourcePath) {
        URL imageResource = Main.class.getResource(resourcePath);
        if (imageResource == null) {
            return null;
        }
        return new Image(imageResource.toExternalForm());
    }
}
