package miku.command;

import miku.MikuException;
import miku.storage.Storage;
import miku.task.Task;
import miku.task.TaskList;
import miku.ui.ResponseFormatter;

/** Adds a parser-created task to Miku's task list. */
public class AddCommand extends Command {
    private final Task task;

    /** Creates a command that adds the given task. */
    public AddCommand(Task task) {
        this.task = task;
    }

    /** Adds the task, persists the updated list, and reports the result. */
    @Override
    public String execute(TaskList tasks, ResponseFormatter responseFormatter, Storage storage) throws MikuException {
        tasks.add(task);
        saveTasks(storage, tasks);
        return responseFormatter.formatTaskAdded(task, tasks.size());
    }
}
