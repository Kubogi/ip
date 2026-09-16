package miku.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/** Displays a compact command row or a response from Miku. */
public class DialogBox extends HBox {
    /** Creates an empty row that a factory method fills with message content. */
    private DialogBox() {
    }

    /** Returns a right-aligned row with the submitted command word emphasized. */
    public static DialogBox getUserDialog(String input) {
        DialogBox dialogBox = new DialogBox();
        dialogBox.setAlignment(Pos.TOP_RIGHT);
        dialogBox.getStyleClass().add("command-row");

        CommandParts parts = splitCommand(input);
        Text speaker = styledText("You  ", "speaker-cue");
        Text leadingWhitespace = new Text(parts.leadingWhitespace());
        Text command = styledText(parts.command(), "command-verb");
        Text arguments = new Text(parts.arguments());
        TextFlow commandText = new TextFlow(speaker, leadingWhitespace, command, arguments);
        commandText.setTextAlignment(TextAlignment.RIGHT);
        commandText.setMaxWidth(Double.MAX_VALUE);
        commandText.getStyleClass().add("command-content");
        HBox.setHgrow(commandText, Priority.ALWAYS);
        dialogBox.getChildren().add(commandText);
        return dialogBox;
    }

    /** Returns a left-aligned reply, with distinct styling when it reports an error. */
    public static DialogBox getMikuDialog(String text, boolean isError) {
        DialogBox dialogBox = new DialogBox();
        dialogBox.setAlignment(Pos.TOP_LEFT);
        dialogBox.getStyleClass().add(isError ? "error-row" : "reply-row");

        Label message = new Label(text);
        message.setWrapText(true);
        message.setMinWidth(0);
        message.setMaxWidth(Double.MAX_VALUE);
        message.getStyleClass().add("reply-content");
        dialogBox.getChildren().add(message);
        return dialogBox;
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
