package miku.task;

import java.util.ArrayList;
import java.util.List;

/** Owns Miku's ordered collection of tasks and its basic list operations. */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /** Creates a task list containing a copy of the supplied tasks. */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Returns the task at a zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Removes and returns the task at a zero-based index. */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /** Returns the current number of tasks. */
    public int size() {
        return tasks.size();
    }

    /** Returns an immutable snapshot for collaborators that only need to read tasks. */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
