package miku.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import miku.parser.Parser;

/** Displays a compact command row or a response from Miku. */
public class DialogBox extends HBox {
    private static final double AVATAR_SIZE = 48;
    private static final double AVATAR_CORNER_SIZE = 12;
    private static final double MESSAGE_GAP = 8;
    private static final Pattern ARGUMENT_PARTS = Pattern.compile("\\s+|\\S+");

    /** Creates an empty row that a factory method fills with message content. */
    private DialogBox() {
        setSpacing(MESSAGE_GAP);
    }

    /** Returns a right-aligned row that emphasizes recognized command words and their markers. */
    public static DialogBox getUserDialog(String input, Image avatarImage) {
        DialogBox dialogBox = new DialogBox();
        dialogBox.setAlignment(Pos.TOP_RIGHT);
        dialogBox.getStyleClass().add("command-row");

        CommandParts parts = splitCommand(input);
        boolean shouldHighlightSyntax = Parser.isRecognizedCommandWord(parts.command());
        TextFlow commandText = new TextFlow(new Text(parts.leadingWhitespace()));
        Text command = new Text(parts.command());
        if (shouldHighlightSyntax) {
            command.getStyleClass().add("command-verb");
        }
        commandText.getChildren().add(command);
        for (ArgumentPart part : splitArguments(parts.command(), parts.arguments(), shouldHighlightSyntax)) {
            Text argument = new Text(part.text());
            if (part.isMarker()) {
                argument.getStyleClass().add("command-marker");
            }
            commandText.getChildren().add(argument);
        }
        commandText.setTextAlignment(TextAlignment.RIGHT);
        commandText.setMinWidth(0);
        commandText.setMaxWidth(Double.MAX_VALUE);
        commandText.getStyleClass().add("command-content");
        dialogBox.getChildren().addAll(commandText, createAvatar(avatarImage));
        return dialogBox;
    }

    /** Returns a left-aligned reply that emphasizes only an error's opening word. */
    public static DialogBox getMikuDialog(String text, boolean isError, Image avatarImage) {
        DialogBox dialogBox = new DialogBox();
        dialogBox.setAlignment(Pos.TOP_LEFT);
        dialogBox.getStyleClass().add("reply-row");

        ReplyParts parts = splitReply(text, isError);
        TextFlow message = new TextFlow();
        if (!parts.highlightedPrefix().isEmpty()) {
            message.getChildren().add(styledText(parts.highlightedPrefix(), "oops-prefix"));
        }
        message.getChildren().add(new Text(parts.remainingText()));
        message.setMinWidth(0);
        message.setMaxWidth(Double.MAX_VALUE);
        message.getStyleClass().add("reply-content");
        dialogBox.getChildren().addAll(createAvatar(avatarImage), message);
        return dialogBox;
    }

    /** Separates a known error prefix while preserving every character of the reply. */
    static ReplyParts splitReply(String text, boolean isError) {
        if (isError && text.startsWith(ResponseFormatter.ERROR_PREFIX)) {
            return new ReplyParts(ResponseFormatter.ERROR_PREFIX,
                    text.substring(ResponseFormatter.ERROR_PREFIX.length()));
        }
        return new ReplyParts("", text);
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

    /** Splits arguments into exact text runs and marks supported syntax delimiters. */
    static List<ArgumentPart> splitArguments(String command, String arguments, boolean shouldHighlightSyntax) {
        List<ArgumentPart> parts = new ArrayList<>();
        Matcher matcher = ARGUMENT_PARTS.matcher(arguments);
        while (matcher.find()) {
            String part = matcher.group();
            parts.add(new ArgumentPart(part, shouldHighlightSyntax && isMarkerForCommand(command, part)));
        }
        return List.copyOf(parts);
    }

    /** Returns whether a delimiter is meaningful for the submitted command word. */
    private static boolean isMarkerForCommand(String command, String marker) {
        return switch (command) {
            case "deadline" -> marker.equals("/by");
            case "event" -> marker.equals("/from") || marker.equals("/to");
            case "reschedule" -> marker.equals("/by") || marker.equals("/from") || marker.equals("/to");
            default -> false;
        };
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

    /** Holds an unchanged argument run and whether it is a syntax delimiter. */
    record ArgumentPart(String text, boolean isMarker) {
    }

    /** Holds the styled prefix and remaining reply text. */
    record ReplyParts(String highlightedPrefix, String remainingText) {
    }
}
