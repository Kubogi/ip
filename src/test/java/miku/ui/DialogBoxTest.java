package miku.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Tests the command display text used in user message rows. */
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
    void splitCommand_unknownCommand_stillEmphasizesFirstWord() {
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
}
