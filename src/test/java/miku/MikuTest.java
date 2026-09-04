package miku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import miku.storage.Storage;

/** Tests Miku's response API that connects command processing to the GUI. */
class MikuTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void getResponse_addAndListCommands_returnsResponsesAndPersistsTask() throws IOException {
        Path saveFile = temporaryDirectory.resolve("miku.json");
        Miku miku = new Miku(new Storage(saveFile));

        String addResponse = miku.getResponse("todo read book");
        String listResponse = miku.getResponse("list");

        assertEquals("Got it! I've added this task for you ✨\n[T][ ] read book\n"
                + "Now you have 1 task(s) in the list! ☆", addResponse);
        assertEquals("Here are the tasks in your list ♫\n1.[T][ ] read book", listResponse);
        assertTrue(Files.readString(saveFile).contains("read book"));
    }

    @Test
    void getResponse_blankInput_returnsErrorWithoutChangingTasks() {
        Miku miku = new Miku(new Storage(temporaryDirectory.resolve("miku.json")));

        String errorResponse = miku.getResponse("   ");
        String listResponse = miku.getResponse("list");

        assertEquals("OOPS!!! The command cannot be empty. Please tell Miku what to do ♪", errorResponse);
        assertEquals("Here are the tasks in your list ♫", listResponse);
    }

    @Test
    void getResponse_byeCommand_returnsFarewellAndRequestsExit() {
        Miku miku = new Miku(new Storage(temporaryDirectory.resolve("miku.json")));

        String response = miku.getResponse("bye");

        assertEquals("Bye bye! Miku hopes to see you again soon! ✨", response);
        assertTrue(miku.isExitRequested());
    }

    @Test
    void getWelcomeMessage_invalidSavedTasks_includesLoadingWarning() throws IOException {
        Path saveFile = temporaryDirectory.resolve("miku.json");
        Files.writeString(saveFile, "not json");

        Miku miku = new Miku(new Storage(saveFile));

        assertTrue(miku.getWelcomeMessage().contains("could not load saved tasks"));
        assertFalse(miku.isExitRequested());
    }
}
