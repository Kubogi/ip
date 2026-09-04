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

    /** Creates a right-aligned message box with the supplied text and optional avatar. */
    public DialogBox(String text, Image image) {
        Label dialog = new Label(text);
        ImageView displayPicture = new ImageView(image);

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
}
