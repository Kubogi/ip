package miku.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/** Tests the text segments used in command and reply bubbles. */
class DialogBoxTest {
    @Test
    void splitCommand_leadingSpaceAndArguments_preservesSubmittedText() {
        String input = "  deadline submit report /by 2026-09-20  ";

        DialogBox.CommandParts parts = DialogBox.splitCommand(input);

        assertEquals("  ", parts.leadingWhitespace());
        assertEquals("deadline", parts.command());
        assertEquals(" submit report /by 2026-09-20  ", parts.arguments());
        assertEquals(input, parts.leadingWhitespace() + parts.command() + parts.arguments());
    }

    @Test
    void splitCommand_unknownCommand_preservesFirstWord() {
        DialogBox.CommandParts parts = DialogBox.splitCommand("sparkle later");

        assertEquals("sparkle", parts.command());
        assertEquals(" later", parts.arguments());
    }

    @Test
    void splitCommand_commandWithoutArguments_hasEmptyRemainder() {
        DialogBox.CommandParts parts = DialogBox.splitCommand("list");

        assertEquals("list", parts.command());
        assertEquals("", parts.arguments());
    }

    @Test
    void splitArguments_deadlineMarker_preservesTextAndMarksBy() {
        String arguments = " submit report /by 2026-09-20";

        List<DialogBox.ArgumentPart> parts = DialogBox.splitArguments("deadline", arguments, true);

        assertEquals(arguments, parts.stream().map(DialogBox.ArgumentPart::text).reduce("", String::concat));
        assertEquals(List.of("/by"), parts.stream().filter(DialogBox.ArgumentPart::isMarker)
                .map(DialogBox.ArgumentPart::text).toList());
    }

    @Test
    void splitArguments_eventMarkers_marksFromAndToOnly() {
        List<DialogBox.ArgumentPart> parts = DialogBox.splitArguments("event",
                " rehearsal /from 2026-09-20 /to 2026-09-21 /by note", true);

        assertEquals(List.of("/from", "/to"), parts.stream().filter(DialogBox.ArgumentPart::isMarker)
                .map(DialogBox.ArgumentPart::text).toList());
    }

    @Test
    void splitArguments_rescheduleMarkers_marksBothSupportedForms() {
        List<DialogBox.ArgumentPart> deadline = DialogBox.splitArguments("reschedule",
                " 2 /by 2026-09-20", true);
        List<DialogBox.ArgumentPart> event = DialogBox.splitArguments("reschedule",
                " 2 /from 2026-09-20 /to 2026-09-21", true);

        assertEquals(List.of("/by"), deadline.stream().filter(DialogBox.ArgumentPart::isMarker)
                .map(DialogBox.ArgumentPart::text).toList());
        assertEquals(List.of("/from", "/to"), event.stream().filter(DialogBox.ArgumentPart::isMarker)
                .map(DialogBox.ArgumentPart::text).toList());
    }

    @Test
    void splitArguments_literalMarkerOrInvalidCommand_doesNotMarkSyntax() {
        List<DialogBox.ArgumentPart> literal = DialogBox.splitArguments("todo", " write /by hand", true);
        List<DialogBox.ArgumentPart> invalid = DialogBox.splitArguments("deadline", " report /by 2026-09-20", false);

        assertTrue(literal.stream().noneMatch(DialogBox.ArgumentPart::isMarker));
        assertTrue(invalid.stream().noneMatch(DialogBox.ArgumentPart::isMarker));
    }

    @Test
    void splitReply_loadingWarning_highlightsOnlyPrefixAndPreservesText() {
        String warning = new ResponseFormatter().formatLoadingError();

        DialogBox.ReplyParts parts = DialogBox.splitReply(warning, true);

        assertEquals("OOPS!!!", parts.highlightedPrefix());
        assertEquals(warning, parts.highlightedPrefix() + parts.remainingText());
    }

    @Test
    void splitReply_ordinaryOopsMessage_hasNoHighlightedPrefix() {
        String message = "Oops... I've marked this task as not done yet ♪";

        DialogBox.ReplyParts parts = DialogBox.splitReply(message, false);

        assertEquals("", parts.highlightedPrefix());
        assertEquals(message, parts.remainingText());
    }

    @Test
    void splitReply_errorWithoutKnownPrefix_keepsWholeMessageReadable() {
        String message = "Something went wrong";

        DialogBox.ReplyParts parts = DialogBox.splitReply(message, true);

        assertEquals("", parts.highlightedPrefix());
        assertEquals(message, parts.remainingText());
    }
}
