package miku.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/** Tests task-list operations that support Miku's commands. */
class TaskListTest {

    @Test
    void findByDescription_mixedCaseKeyword_returnsOnlyMatchingTasksInListOrder() {
        TaskList tasks = new TaskList(List.of(
                new Todo("read book"),
                new Todo("buy groceries"),
                new Todo("return BOOK")));

        List<Task> matches = tasks.findByDescription("BoOk");

        assertEquals(2, matches.size());
        assertEquals("read book", matches.get(0).getDescription());
        assertEquals("return BOOK", matches.get(1).getDescription());
    }

    @Test
    void findByDescription_noMatch_returnsEmptyList() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        assertEquals(List.of(), tasks.findByDescription("meeting"));
    }
}
