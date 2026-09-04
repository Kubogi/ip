package miku.command;

import miku.MikuException;
import miku.storage.Storage;
import miku.task.Task;
import miku.task.TaskList;
import miku.ui.ResponseFormatter;

/** Removes one task from Miku's task list. */
public class DeleteCommand extends TaskNumberCommand {
    /** Creates a command targeting a one-based task number. */
    public DeleteCommand(int taskNumber) {
        super(taskNumber);
    }

    /** Removes the selected task, persists the updated list, and reports the result. */
    @Override
    public String execute(TaskList tasks, ResponseFormatter responseFormatter, Storage storage) throws MikuException {
        Task removedTask = tasks.remove(getTaskIndex(tasks));
        saveTasks(storage, tasks);
        return responseFormatter.formatTaskDeleted(removedTask, tasks.size());
    }
}
