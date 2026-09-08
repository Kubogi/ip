package miku.storage;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import miku.task.Task;

/** Tests assumptions that protect Storage's fixed task schema. */
class StorageTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void saveTasks_unsupportedTaskType_assertionThrown() {
        Storage storage = new Storage(temporaryDirectory.resolve("miku.json"));

        assertThrows(AssertionError.class, () -> storage.saveTasks(List.of(new OtherTask())));
    }

    /** Represents a future task subtype that Storage has not yet learned to serialize. */
    private static class OtherTask extends Task {
        private OtherTask() {
            super("T", "unserializable task");
        }
    }
}
