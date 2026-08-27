package miku.command;

import miku.MikuException;
import miku.storage.Storage;
import miku.task.Task;
import miku.task.TaskList;
import miku.ui.Ui;

/** Marks one task as complete. */
public class MarkCommand extends TaskNumberCommand {
    /** Creates a command targeting a one-based task number. */
    public MarkCommand(int taskNumber) {
        super(taskNumber);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws MikuException {
        Task task = getTask(tasks);
        task.markAsDone();
        saveTasks(storage, tasks);
        ui.showTaskMarked(task);
    }
}
