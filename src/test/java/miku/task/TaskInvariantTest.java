package miku.task;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/** Tests the task-model assumptions checked during development. */
class TaskInvariantTest {

    @Test
    void todo_nullDescription_assertionThrown() {
        assertThrows(AssertionError.class, () -> new Todo(null));
    }

    @Test
    void deadline_nullDateTime_assertionThrown() {
        assertThrows(AssertionError.class, () -> new Deadline("submit work", null, false));
    }

    @Test
    void event_nullEndpoint_assertionThrown() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 9, 8, 10, 0);

        assertThrows(AssertionError.class, () -> new Event("concert", null, false, dateTime, true));
        assertThrows(AssertionError.class, () -> new Event("concert", dateTime, true, null, false));
    }
}
