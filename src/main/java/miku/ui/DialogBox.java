package miku.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/** Displays one chat message alongside an optional speaker avatar. */
public class DialogBox extends HBox {
    private static final double AVATAR_SIZE = 64;
    private final Label dialog;
    private final ImageView displayPicture;

    /** Creates a right-aligned message box with the supplied text and optional avatar. */
    public DialogBox(String text, Image image) {
        dialog = new Label(text);
        displayPicture = new ImageView(image);

        dialog.setWrapText(true);
        dialog.setMaxWidth(290);
        displayPicture.setFitHeight(AVATAR_SIZE);
        displayPicture.setFitWidth(AVATAR_SIZE);
        displayPicture.setPreserveRatio(true);
        if (image == null) {
            displayPicture.setManaged(false);
            displayPicture.setVisible(false);
        }

        setAlignment(Pos.TOP_RIGHT);
        setPadding(new Insets(10, 5, 10, 5));
        getChildren().addAll(dialog, displayPicture);
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
