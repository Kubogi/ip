package miku.parser;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import miku.MikuException;
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
}
