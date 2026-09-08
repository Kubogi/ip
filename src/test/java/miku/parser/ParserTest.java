package miku.parser;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import miku.MikuException;
import miku.command.AddCommand;
import miku.command.FindCommand;

/** Tests parser validation for commands with high-value input rules. */
class ParserTest {

    @Test
    void parse_findWithKeyword_returnsFindCommand() throws MikuException {
        Parser parser = new Parser();

        assertInstanceOf(FindCommand.class, parser.parse("find book"));
    }

    @Test
    void parse_findWithoutKeyword_exceptionThrown() {
        Parser parser = new Parser();

        assertThrows(MikuException.class, () -> parser.parse("find"));
    }

    @Test
    void parse_taskCommands_returnsAddCommands() throws MikuException {
        Parser parser = new Parser();

        assertInstanceOf(AddCommand.class, parser.parse("todo review notes"));
        assertInstanceOf(AddCommand.class, parser.parse("deadline submit work /by 2026-09-08"));
        assertInstanceOf(AddCommand.class, parser.parse("event concert /from 2026-09-08 /to 2026-09-09"));
    }

    @Test
    void parse_eventWithEqualEndpoints_returnsAddCommand() throws MikuException {
        Parser parser = new Parser();

        assertInstanceOf(AddCommand.class, parser.parse("event briefing /from 2026-09-08 0900 /to 2026-09-08 0900"));
    }

    @Test
    void parse_eventEndingBeforeStart_exceptionThrown() {
        Parser parser = new Parser();

        assertThrows(MikuException.class, () -> parser.parse(
                "event briefing /from 2026-09-08 0900 /to 2026-09-08 0859"));
    }
}
