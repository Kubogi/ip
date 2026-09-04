package miku.command;

import miku.MikuException;
import miku.storage.Storage;
import miku.task.Task;
import miku.task.TaskList;
import miku.ui.ResponseFormatter;

/** Marks one task as complete. */
public class MarkCommand extends TaskNumberCommand {
    /** Creates a command targeting a one-based task number. */
    public MarkCommand(int taskNumber) {
        super(taskNumber);
    }

    /** Marks the selected task complete, persists the change, and reports the result. */
    @Override
    public String execute(TaskList tasks, ResponseFormatter responseFormatter, Storage storage) throws MikuException {
        Task task = getTask(tasks);
        task.markAsDone();
        saveTasks(storage, tasks);
        return responseFormatter.formatTaskMarked(task);
    }
}
