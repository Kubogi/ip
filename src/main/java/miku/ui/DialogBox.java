package miku.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/** Displays a compact command row or a response from Miku. */
public class DialogBox extends HBox {
    private static final double AVATAR_SIZE = 48;
    private static final double AVATAR_CORNER_SIZE = 12;
    private static final double MESSAGE_GAP = 8;

    /** Creates an empty row that a factory method fills with message content. */
    private DialogBox() {
        setSpacing(MESSAGE_GAP);
    }

    /** Returns a right-aligned row with the submitted command word emphasized. */
    public static DialogBox getUserDialog(String input, Image avatarImage) {
        DialogBox dialogBox = new DialogBox();
        dialogBox.setAlignment(Pos.TOP_RIGHT);
        dialogBox.getStyleClass().add("command-row");

        CommandParts parts = splitCommand(input);
        Text leadingWhitespace = new Text(parts.leadingWhitespace());
        Text command = styledText(parts.command(), "command-verb");
        Text arguments = new Text(parts.arguments());
        TextFlow commandText = new TextFlow(leadingWhitespace, command, arguments);
        commandText.setTextAlignment(TextAlignment.RIGHT);
        commandText.setMinWidth(0);
        commandText.setMaxWidth(Double.MAX_VALUE);
        commandText.getStyleClass().add("command-content");
        dialogBox.getChildren().addAll(commandText, createAvatar(avatarImage));
        return dialogBox;
    }

    /** Returns a left-aligned reply, with distinct styling when it reports an error. */
    public static DialogBox getMikuDialog(String text, boolean isError, Image avatarImage) {
        DialogBox dialogBox = new DialogBox();
        dialogBox.setAlignment(Pos.TOP_LEFT);
        dialogBox.getStyleClass().add(isError ? "error-row" : "reply-row");

        Label message = new Label(text);
        message.setWrapText(true);
        message.setMinWidth(0);
        message.setMaxWidth(Double.MAX_VALUE);
        message.getStyleClass().add("reply-content");
        dialogBox.getChildren().addAll(createAvatar(avatarImage), message);
        return dialogBox;
    }

    /** Creates a small rounded avatar, omitting its layout space when no image is available. */
    private static ImageView createAvatar(Image image) {
        ImageView avatar = new ImageView(image);
        avatar.setFitWidth(AVATAR_SIZE);
        avatar.setFitHeight(AVATAR_SIZE);
        avatar.setPreserveRatio(true);
        Rectangle clip = new Rectangle(AVATAR_SIZE, AVATAR_SIZE);
        clip.setArcWidth(AVATAR_CORNER_SIZE);
        clip.setArcHeight(AVATAR_CORNER_SIZE);
        avatar.setClip(clip);
        if (image == null) {
            avatar.setManaged(false);
            avatar.setVisible(false);
        }
        return avatar;
    }

    /** Splits a command for display without changing the submitted text. */
    static CommandParts splitCommand(String input) {
        int commandStart = 0;
        while (commandStart < input.length() && Character.isWhitespace(input.charAt(commandStart))) {
            commandStart++;
        }
        int commandEnd = commandStart;
        while (commandEnd < input.length() && !Character.isWhitespace(input.charAt(commandEnd))) {
            commandEnd++;
        }
        return new CommandParts(input.substring(0, commandStart), input.substring(commandStart, commandEnd),
                input.substring(commandEnd));
    }

    /** Creates text with one CSS role in the command row. */
    private static Text styledText(String content, String styleClass) {
        Text text = new Text(content);
        text.getStyleClass().add(styleClass);
        return text;
    }

    /** Holds the unchanged pieces of a submitted command. */
    record CommandParts(String leadingWhitespace, String command, String arguments) {
    }
}
