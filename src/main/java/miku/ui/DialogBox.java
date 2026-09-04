package miku.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/** Displays one chat message alongside an optional speaker avatar. */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /** Loads the dialog FXML and fills it with the supplied message and optional avatar. */
    private DialogBox(String text, Image image) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogBox.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("A chat dialog could not be loaded.", exception);
        }
        dialog.setText(text);
        displayPicture.setImage(image);
        if (image == null) {
            displayPicture.setManaged(false);
            displayPicture.setVisible(false);
        }
    }

    /** Returns a right-aligned dialog for a user message. */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, image);
    }

    /** Returns a left-aligned dialog for a response from Miku. */
    public static DialogBox getMikuDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);
        dialogBox.flip();
        return dialogBox;
    }

    /** Moves the avatar to the left and aligns the dialog with Miku's responses. */
    private void flip() {
        setAlignment(Pos.TOP_LEFT);
        getChildren().setAll(displayPicture, dialog);
        dialog.getStyleClass().add("reply-label");
    }
}
